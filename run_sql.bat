@echo off
cd /d "D:\project\博客项目app\miniblog-backend"
python.exe run_sql.py
if errorlevel 1 echo FAILED
