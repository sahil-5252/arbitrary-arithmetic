import subprocess
import sys
import os

# Input validation
if len(sys.argv) != 5:
    print("Usage: python run_inf_arith.py <int/float> <add/sub/mul/div> <num1> <num2>")
    sys.exit(1)

java_files = [
    "arbitraryarithmetic/AInteger.java",
    "arbitraryarithmetic/AFloat.java",
    "MyInfArith.java"
]

# Compile Java files
compile_cmd = ["javac"] + java_files
try:
    subprocess.run(compile_cmd, check=True)
except subprocess.CalledProcessError:
    print("Compilation failed")
    sys.exit(1)

# Run the Java class
run_cmd = [
    "java", "MyInfArith",
    sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]
]

subprocess.run(run_cmd)
