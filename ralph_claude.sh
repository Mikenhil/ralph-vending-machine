#!/bin/bash
set -e

if [ -z "$1" ]; then
  echo "Usage: $0 <iterations>"
  exit 1
fi

for ((i=1; i<=$1; i++)); do
  echo ""
  echo "****************************************"
  echo "Iteration $i of $1"
  echo "****************************************"

  result=$(claude --dangerously-skip-permissions -p "@CLAUDE.md @PRD.md @progress.txt \
1. Read progress.txt to see what has been completed. \
2. Find the highest-priority incomplete task from PRD.md. \
3. Implement that single task. \
4. Write unit tests for any code with logic. \
5. Verify: ./mvnw compile -q && ./mvnw test -q \
6. If tests pass, git add and commit with a conventional commit message (feat:, fix:, test:, refactor:). \
7. Mark the task complete in PRD.md (change [ ] to [x]). \
8. Append your progress to progress.txt with what you completed. \
9. If ALL tasks in PRD.md are complete, output <promise>COMPLETE</promise>. \
ONLY WORK ON ONE TASK PER ITERATION.")

  echo "$result"

  if [[ "$result" == *"<promise>COMPLETE</promise>"* ]]; then
    echo ""
    echo "✅ PRD complete after $i iterations!"
    exit 0
  fi
done

echo ""
echo "⚠️ Reached $1 iterations. PRD may not be complete."