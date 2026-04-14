#!/bin/bash
# ralph.sh - persistent iteration using Ollama

MODEL="qwen2.5-coder:7b" # Recommended for coding tasks
TASK_FILE="PRD.md"
ITERATION=0
MAX_ITERATIONS=10

while [ $ITERATION -lt $MAX_ITERATIONS ]; do
  echo "--- Ralph Iteration: $ITERATION ---"

  # Feed the task file and current codebase state to Ollama
  # The model is instructed to perform ONE task and update the file
  RESPONSE=$(ollama run $MODEL "Read $TASK_FILE.
    Find the first unchecked task.
    Execute it, update $TASK_FILE by marking it [x],
    and output 'COMPLETE' if everything is done.")

  echo "$RESPONSE"

  # Check for completion signal
  if [[ "$RESPONSE" == *"COMPLETE"* ]]; then
    echo "Task fully completed!"
    break
  fi

  # (Optional) Commit changes to Git for persistent state/memory
  git add . && git commit -m "Ralph iteration $ITERATION"

  ITERATION=$((ITERATION + 1))
done
