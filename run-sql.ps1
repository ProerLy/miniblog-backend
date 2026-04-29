$sql = Get-Content "D:\project\博客项目app\miniblog-backend\sql\init.sql" -Raw -Encoding UTF8
$psi = New-Object System.Diagnostics.ProcessStartInfo
$psi.FileName = "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe"
$psi.Arguments = "-h localhost -P 3306 -u root -proot"
$psi.RedirectStandardInput = $true
$psi.RedirectStandardOutput = $true
$psi.RedirectStandardError = $true
$psi.UseShellExecute = $false
$psi.CreateNoWindow = $true
$proc = [System.Diagnostics.Process]::Start($psi)
$proc.StandardInput.Write($sql)
$proc.StandardInput.Close()
$stdout = $proc.StandardOutput.ReadToEnd()
$stderr = $proc.StandardError.ReadToEnd()
$proc.WaitForExit()
Write-Host "EXIT: $($proc.ExitCode)"
if ($stdout) { Write-Host "STDOUT: $stdout" }
if ($stderr) { Write-Host "STDERR: $stderr" }
