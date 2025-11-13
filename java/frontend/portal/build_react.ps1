Write-Host "[INFO] Building React scripts built"
npm.cmd run build

Write-Host "[INFO] React scripts built, Copying files from dist to springboot static directory"
copy dist\* -Destination ..\..\src\main\resources\static\. -Recurse -Force
Write-Host "[INFO] Copied files from dist to springboot static directory "
