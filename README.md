# Cadena de frío

Microservicio REST para registrar envíos refrigerados y supervisar eventos térmicos
durante el transporte de leche, frutas y otros productos sensibles.

## Requisitos

- Java 17
- Docker Desktop
- kubectl (para Kubernetes)
- Maven no es necesario: el proyecto incluye Maven Wrapper.

## Ejecución local con H2

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

## Ejecución con Docker Compose y PostgreSQL

Crear el archivo local de secretos:

```powershell
Copy-Item .env.example .env
notepad .env
```

Definir en `.env` una clave real de reCAPTCHA:

```text
CAPTCHA_SECRET=TU_SECRET_KEY
```

Luego iniciar los servicios:

```powershell
docker compose up --build
```

La aplicación queda en `http://localhost:8080` y PostgreSQL en el puerto `5432`.

Para comprobar el estado:

```powershell
docker compose ps
```

## Despliegue en Kubernetes local

Se requiere tener Kubernetes habilitado en Docker Desktop.

```powershell
kubectl apply -f k8s\postgres.yaml

$values = Get-Content .env | Where-Object { $_ -match '^\s*[^#].*=.*$' } | ConvertFrom-StringData
kubectl create secret generic cadenafrio-app-secret `
  --from-literal=CAPTCHA_SECRET=$values.CAPTCHA_SECRET `
  --dry-run=client -o yaml | kubectl apply -f -

docker build -t cadenafrio-app:latest .
kubectl apply -f k8s\app.yaml
kubectl get pods
```

Para probar la API desde el equipo local:

```powershell
kubectl port-forward service/cadenafrio-app 18080:8080
```

Usar entonces `http://localhost:18080` en Postman.

## Endpoints principales

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/envios` | Crear un envío |
| GET | `/api/envios/{id}` | Consultar un envío |
| POST | `/api/envios/{id}/eventos` | Registrar una lectura térmica |
| GET | `/api/envios/{id}/eventos` | Consultar el historial |
| GET | `/api/envios/{id}/alertas` | Consultar lecturas fuera de rango |

La creación de envíos requiere el encabezado `X-Captcha-Token`.

## Seguridad

Los secretos reales deben permanecer en `.env` o en Kubernetes Secrets.
`.env.example` solo contiene la estructura de configuración.
