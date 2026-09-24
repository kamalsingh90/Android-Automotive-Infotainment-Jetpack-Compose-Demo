#!/usr/bin/env python3
"""
AutoConnect Cold Startup Performance Analyzer
Parses startup.log to calculate statistics on application startup latency.
"""

import sys
import os

def analyze_startup(log_path="startup.log"):
    if not os.path.exists(log_path):
        print(f"Error: Log file '{log_path}' not found.", file=sys.stderr)
        return False

    samples = []
    try:
        with open(log_path, "r", encoding="utf-8") as f:
            for line_num, line in enumerate(f, 1):
                clean_line = line.strip()
                if not clean_line or clean_line.startswith("#"):
                    continue
                try:
                    val = float(clean_line)
                    if val > 0:
                        samples.append(val)
                except ValueError:
                    print(f"Warning: Skipping invalid line {line_num}: '{clean_line}'", file=sys.stderr)
    except Exception as e:
        print(f"Error reading file: {e}", file=sys.stderr)
        return False

    if not samples:
        print(f"Error: Log file '{log_path}' contains no valid numerical samples.", file=sys.stderr)
        return False

    count = len(samples)
    min_val = min(samples)
    max_val = max(samples)
    avg_val = sum(samples) / count

    print("Startup Performance Report")
    print()
    print(f"Samples: {count}")
    print(f"Minimum: {int(min_val) if min_val.is_integer() else min_val:.1f} ms")
    print(f"Maximum: {int(max_val) if max_val.is_integer() else max_val:.1f} ms")
    print(f"Average: {avg_val:.1f} ms")
    return True

if __name__ == "__main__":
    target_path = sys.argv[1] if len(sys.argv) > 1 else "startup.log"
    success = analyze_startup(target_path)
    sys.exit(0 if success else 1)
