import subprocess, sys

mysql_bin = r"C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe"
sql_file = r"D:\project\博客项目app\miniblog-backend\sql\init.sql"
out_file = r"D:\project\博客项目app\miniblog-backend\sql_out.txt"

with open(sql_file, "r", encoding="utf-8") as f:
    content = f.read()

# Remove USE miniblog; since we'll pass --database
statements = []
current = []
in_use = False
for line in content.splitlines():
    stripped = line.strip()
    if stripped.startswith("--") or stripped.startswith("/*") or not stripped:
        continue
    if stripped.upper().startswith("USE "):
        in_use = True
        continue
    current.append(line)
    if stripped.endswith(";"):
        stmt = "\n".join(current).strip()
        if stmt:
            statements.append(stmt)
        current = []

out_lines = []
for stmt in statements:
    args = [
        mysql_bin, "-h", "localhost", "-P", "3306",
        "-u", "root", "-proot",
        "--default-character-set=utf8mb4",
        "--database=miniblog",
        "-e", stmt
    ]
    r = subprocess.run(args, capture_output=True, timeout=15)
    err = r.stderr.decode("utf-8", errors="replace").strip()
    if r.returncode != 0:
        out_lines.append("FAIL " + stmt[:60])
        out_lines.append("  -> " + err)
    else:
        if err and "Warning" not in err:
            out_lines.append("WARN " + stmt[:60] + " -> " + err)
        else:
            out_lines.append("OK   " + stmt[:60])

with open(out_file, "w", encoding="utf-8") as f:
    f.write("\n".join(out_lines))

exit_code = sum(1 for l in out_lines if l.startswith("FAIL"))
print("exit_code:" + str(exit_code))
print("Total:", len(statements), "  FAIL:", exit_code)
sys.exit(exit_code)
