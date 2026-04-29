try {
    $resp = Invoke-WebRequest -Uri "http://localhost:9090/categories" -Method GET -TimeoutSec 10
    $status = $resp.StatusCode
    $body = $resp.Content
} catch {
    $status = $_.Exception.Response.StatusCode.value__
    $body = $_.Exception.Message
}
Write-Host "STATUS:$status"
Write-Host "BODY:$body"
