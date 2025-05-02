import subprocess
import sys

def build_and_run(data_type, operation, operand1, operand2):
    # Optional: write dynamic args to a temporary build.xml if needed
    cmd = ["ant", "run"]
    print("Running Ant build...")
    subprocess.run(cmd, check=True)

if __name__ == "__main__":
    if len(sys.argv) != 5:
        print("Usage: python3 runproj.py <int/float> <add/sub/mul/div> <operand1> <operand2>")
        sys.exit(1)

    dtype, op, op1, op2 = sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]

    # You can dynamically generate the Ant XML to pass args
    with open("build.xml", "w") as f:
        f.write(f"""<project name="MyInfArithProject" default="run" basedir=".">
    <property name="src.dir" value="src"/>
    <property name="build.dir" value="build"/>

    <target name="clean">
        <delete dir="${{build.dir}}"/>
    </target>

    <target name="compile">
        <mkdir dir="${{build.dir}}"/>
        <javac srcdir="${{src.dir}}" destdir="${{build.dir}}" includeantruntime="false"/>
    </target>

    <target name="run" depends="compile">
        <java classname="MyInfArith" classpath="${{build.dir}}" fork="true">
            <arg value="{dtype}"/>
            <arg value="{op}"/>
            <arg value="{op1}"/>
            <arg value="{op2}"/>
        </java>
    </target>
</project>
""")
    build_and_run(dtype, op, op1, op2)
