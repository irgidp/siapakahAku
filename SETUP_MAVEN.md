# Setup Maven & Build Instructions

## Install Maven (Windows)

### Option 1: Download & Setup Manual
1. Download Maven dari https://maven.apache.org/download.cgi  
2. Extract ke folder (e.g., `C:\Apache\maven`)
3. Add ke PATH:
   - System Properties > Environment Variables
   - New SYSTEM variable: `M2_HOME = C:\Apache\maven`
   - Edit PATH, add: `C:\Apache\maven\bin`
4. Verify: Buka command prompt baru, ketik `mvn -version`

### Option 2: Gunakan Chocolatey
```powershell
# Install Chocolatey (jika belum)
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; 
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install Maven
choco install maven
```

### Option 3: Gunakan Scoop
```powershell
scoop install maven
```

## Build Plugin

Setelah Maven terinstall, di project root folder ketik:

```bash
mvn clean package
```

Hasil JAR: `target/siapakahAku-1.0-SNAPSHOT.jar`

## Quick Test di IDE

Jika ada IDE error, bisa gunakan Maven task di IDE:
- IntelliJ IDEA: View > Tool Windows > Maven, then run `clean package`
- VS Code: Install Extension "Maven for Java", then run command

## Docker Build (Alternative)

Jika Maven installation ribet, gunakan Docker:

```bash
docker run -it -v "H:\Project Minecraft Plugin\siapakahAku":/app -w /app maven:latest mvn clean package
```

## Next Steps

1. ✅ Copy JAR ke `plugins/` folder di Paper server
2. ✅ Restart server
3. ✅ Test commands: `/regis <playername>`

