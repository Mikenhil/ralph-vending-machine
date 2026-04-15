#!/bin/bash
set -e

if [ -z "$1" ]; then
  echo "Usage: $0 <iterations>"
  exit 1
fi

PRD_FILE="PRD.md"
PROGRESS_FILE="progress.txt"

# ── Backup PRD.md before every iteration ─────────────────────────────────────
backup_prd() {
  cp "$PRD_FILE" "${PRD_FILE}.bak"
}

# ── Restore PRD.md if it got corrupted ───────────────────────────────────────
restore_prd_if_corrupted() {
  python3 - <<PYEOF
import re, sys, os

with open('${PRD_FILE}', 'r') as f:
    content = f.read()

tasks = re.findall(r'(?:-|\*)\s+\[\s?[x ]?\s?\]\s+.+', content)
if len(tasks) == 0 and os.path.exists('${PRD_FILE}.bak'):
    print("[ralph] PRD.md appears corrupted — restoring from backup")
    with open('${PRD_FILE}.bak', 'r') as f:
        backup = f.read()
    with open('${PRD_FILE}', 'w') as f:
        f.write(backup)
else:
    print(f"[ralph] PRD.md OK ({len(tasks)} tasks found)")
PYEOF
}

# ── Mark a task complete in PRD.md ────────────────────────────────────────────
mark_task_complete() {
  local task="$1"
  python3 - "$task" <<'PYEOF'
import sys, re

task = sys.argv[1].strip()

with open('PRD.md', 'r') as f:
    lines = f.readlines()

matched = False
for idx, line in enumerate(lines):
    # Must be a checkbox line (incomplete)
    if not re.match(r'\s*(?:-|\*)\s+\[\s?\]\s+', line):
        continue
    # Extract task text from this line
    m = re.match(r'\s*(?:-|\*)\s+\[\s?\]\s+(.+)', line)
    if not m:
        continue
    line_task = m.group(1).strip()

    # Exact match first
    if line_task == task:
        lines[idx] = line.replace('[ ]', '[x]').replace('[]', '[x]')
        matched = True
        break

    # Fuzzy match: 60% word overlap
    words = [w for w in task.split() if len(w) > 3]
    if words:
        hits = sum(1 for w in words if w.lower() in line_task.lower())
        if hits / len(words) >= 0.6:
            lines[idx] = line.replace('[ ]', '[x]').replace('[]', '[x]')
            matched = True
            break

with open('PRD.md', 'w') as f:
    f.writelines(lines)

if matched:
    print(f"[ralph] PRD.md: marked complete -> {task}")
else:
    print(f"[ralph] WARNING: could not match task in PRD.md: {task}")
    print(f"[ralph] PRD.md left unchanged")
PYEOF
}

# ── Get next incomplete task ──────────────────────────────────────────────────
get_next_task() {
  python3 <<'PYEOF'
import re, sys

with open('PRD.md', 'r') as f:
    lines = f.readlines()

for line in lines:
    m = re.match(r'\s*(?:-|\*)\s+\[\s?\]\s+(.+)', line)
    if m:
        print(m.group(1).strip())
        sys.exit(0)

sys.exit(1)
PYEOF
}

# ── Count remaining tasks ─────────────────────────────────────────────────────
count_remaining() {
  python3 <<'PYEOF'
import re

with open('PRD.md', 'r') as f:
    lines = f.readlines()

incomplete = sum(1 for l in lines if re.match(r'\s*(?:-|\*)\s+\[\s?\]\s+', l))
complete   = sum(1 for l in lines if re.match(r'\s*(?:-|\*)\s+\[x\]\s+', l, re.I))
print(f"[ralph] Tasks: {complete} done, {incomplete} remaining")
PYEOF
}

# ── Write files from model output ─────────────────────────────────────────────
parse_and_write_files() {
  local result="$1"
  python3 - "$result" <<'PYEOF'
import sys, re, os

output = sys.argv[1]
wrote = []

def write_file(path, content):
    path = path.strip().strip('"').strip("'")
    if not path or len(path) > 300 or '\n' in path:
        return
    if not re.search(r'\.\w{1,6}$', path):
        return
    # Safety: never overwrite PRD.md or progress.txt
    if os.path.basename(path) in ('PRD.md', 'progress.txt'):
        print(f"[ralph] Skipping protected file: {path}")
        return
    dirpart = os.path.dirname(path)
    if dirpart:
        os.makedirs(dirpart, exist_ok=True)
    with open(path, 'w') as f:
        f.write(content)
    wrote.append(path)
    print(f"[ralph] wrote: {path}")

# Priority 1: <<<FILE: path>>> ... <<<END FILE>>>
for m in re.finditer(r'<<<FILE:\s*(.+?)>>>\n(.*?)<<<END FILE>>>', output, re.DOTALL):
    write_file(m.group(1), m.group(2))

# Priority 2: filename on its own line then fenced block
for m in re.finditer(
    r'(?:^|\n)[#*`\s]*([\w.\-/]+\.\w{1,10})[`*\s]*:?\s*\n```[^\n]*\n(.*?)```',
    output, re.DOTALL
):
    write_file(m.group(1), m.group(2))

# Priority 3: path comment as first line inside fenced block
for m in re.finditer(
    r'```[^\n]*\n(?://|#|<!--|--)\s*([\w./\-]+\.\w{1,10})[^\n]*\n(.*?)```',
    output, re.DOTALL
):
    write_file(m.group(1), m.group(2))

# Priority 4: scan 3 lines before each fenced block for a filepath
lines = output.split('\n')
i = 0
while i < len(lines):
    if lines[i].startswith('```') and i > 0:
        block_lines = []
        j = i + 1
        while j < len(lines) and not lines[j].startswith('```'):
            block_lines.append(lines[j])
            j += 1
        content = '\n'.join(block_lines) + '\n'
        found_path = None
        for k in range(max(0, i-3), i):
            candidate = re.search(r'([\w.\-/]+\.\w{1,10})', lines[k])
            if candidate:
                p = candidate.group(1)
                if '.' in p and not p.startswith('http'):
                    found_path = p
                    break
        if found_path and len(content.strip()) > 10:
            write_file(found_path, content)
        i = j + 1
    else:
        i += 1

if not wrote:
    print("[ralph] No files detected in model output.")
PYEOF
}

# ─────────────────────────────────────────────────────────────────────────────
# MAIN LOOP
# ─────────────────────────────────────────────────────────────────────────────
echo "[ralph] Starting..."
count_remaining

for ((i=1; i<=$1; i++)); do
  echo ""
  echo "****************************************"
  echo "Iteration $i of $1"
  echo "****************************************"

  # Get next task BEFORE calling the model
  NEXT_TASK=$(get_next_task 2>/dev/null) || {
    echo "[ralph] ✅ No incomplete tasks found. Done!"
    exit 0
  }
  echo "[ralph] Working on: $NEXT_TASK"

  # Backup PRD before the model runs
  backup_prd

  PRD=$(cat "$PRD_FILE")
  PROGRESS=$(cat "$PROGRESS_FILE" 2>/dev/null || echo "No progress yet")

  PROMPT="You are an expert Java 25 software engineer implementing one task at a time.

=== ALREADY COMPLETED ===
$PROGRESS

=== PRD ===
$PRD

=== YOUR TASK ===
$NEXT_TASK

=== RULES ===
- Work in package: src/main/java/dev/mikenhil/vending/ollama
- Java files use .java extension only
- Do NOT touch files in the claude package
- Do NOT rewrite or include PRD.md or progress.txt in your output
- Output COMPLETE file contents only — no partial snippets

=== FILE OUTPUT FORMAT ===
For every file you create, use a fenced code block with the file path as the FIRST LINE comment:

\`\`\`java
// src/main/java/dev/mikenhil/vending/ollama/Product.java
package dev.mikenhil.vending.ollama;

// ... full file content
\`\`\`

One block per file. After all blocks write one sentence describing what you did."

  result=$(echo "$PROMPT" | ollama run codellama:7b)

  echo "$result"

  # Restore PRD if model corrupted it
  restore_prd_if_corrupted

  # Write java files from output
  parse_and_write_files "$result"

  # Mark task complete directly in PRD.md
  mark_task_complete "$NEXT_TASK"

  # Append to progress.txt — strip code blocks to keep it concise
  {
    echo ""
    echo "--- Iteration $i: $NEXT_TASK ---"
    echo "$result" | python3 -c "
import sys, re
text = sys.stdin.read()
text = re.sub(r'\`\`\`.*?\`\`\`', '[code omitted]', text, flags=re.DOTALL)
print(text[:600])
"
  } >> "$PROGRESS_FILE"

  count_remaining

  if [[ "$result" == *"<promise>COMPLETE</promise>"* ]]; then
    echo ""
    echo "✅ PRD complete after $i iterations!"
    exit 0
  fi

done

echo ""
echo "⚠️  Reached $1 iterations. PRD may not be complete."