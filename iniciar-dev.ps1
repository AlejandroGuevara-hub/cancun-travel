$ErrorActionPreference = 'Stop'
foreach ($name in @('DB_URL', 'DB_USERNAME', 'DB_PASSWORD')) {
    if ([string]::IsNullOrWhiteSpace([Environment]::GetEnvironmentVariable($name, 'Process'))) {
        throw "Configure la variable de entorno $name antes de iniciar. Consulte README.md."
    }
}
$previousProfile = [Environment]::GetEnvironmentVariable('SPRING_PROFILES_ACTIVE', 'Process')
Push-Location $PSScriptRoot
try {
    $env:SPRING_PROFILES_ACTIVE = 'dev'
    & "$PSScriptRoot\mvnw.cmd" spring-boot:run
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo iniciar la aplicacion.' }
}
finally {
    [Environment]::SetEnvironmentVariable('SPRING_PROFILES_ACTIVE', $previousProfile, 'Process')
    Pop-Location
}
