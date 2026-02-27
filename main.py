from fastapi import FastAPI, Header, HTTPException
import requests
from prometheus_fastapi_instrumentator import Instrumentator

app = FastAPI(title="Cloud Sim Bank API")

Instrumentator().instrument(app).expose(app)

ACCOUNT_SERVICE_URL = "http://account-service:8081/accounts"

@app.get("/")
def root():
    return {
        "service": "Cloud Sim Bank API",
        "role": "Banking Facade"
    }

@app.get("/accounts")
def get_accounts(
    x_consumer_username: str = Header(default="guest"),
    x_jwt_claims_age: int = Header(default=21)
):
    if x_jwt_claims_age < 18 or x_jwt_claims_age > 34:
        raise HTTPException(status_code=403, detail="Youth product only")

    response = requests.get(ACCOUNT_SERVICE_URL)

    return {
        "customer": x_consumer_username,
        "accounts": response.json()
    }