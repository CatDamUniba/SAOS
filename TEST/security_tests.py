import requests
import jwt
import datetime

# Configurazione base
BASE_URL = "http://127.0.0.1:8080"
LOGIN_ENDPOINT = f"{BASE_URL}/api/auth/login"
BOOKS_ENDPOINT = f"{BASE_URL}/api/libro/findAllBooks"

USERNAME = "admin"      # Cambia con un utente admin reale
PASSWORD = "admin123"   # Cambia con la password reale
SECRET_KEY = "sRSecretKey"  # Deve corrispondere alla tua jwtSecret se usi HS512

# Funzione login per ottenere un JWT valido
def get_valid_jwt():
    res = requests.post(LOGIN_ENDPOINT, json={"username": USERNAME, "password": PASSWORD})
    if res.status_code == 200:
        data = res.json()
        return data["accessToken"]
    else:
        print("❌ Login fallito:", res.status_code, res.text)
        return None

# Funzione per testare chiamata con un token arbitrario
def test_token(token, description):
    print(f"\n🔹 Test: {description}")
    headers = {"Authorization": f"Bearer {token}"}
    res = requests.get(BOOKS_ENDPOINT, headers=headers)
    print(f"Status code: {res.status_code}")
    if res.status_code == 200:
        print("✅ Accesso consentito")
    else:
        print("❌ Accesso negato:", res.text[:200])

def main():
    # 1. Otteniamo un token valido
    valid_token = get_valid_jwt()
    if not valid_token:
        return

    # Test base con token valido
    test_token(valid_token, "Token valido")

    # 2. Token scaduto (exp passato)
    expired_token = jwt.encode(
        {
            "sub": USERNAME,
            "iat": datetime.datetime.utcnow() - datetime.timedelta(hours=2),
            "exp": datetime.datetime.utcnow() - datetime.timedelta(hours=1),
        },
        SECRET_KEY,
        algorithm="HS512"
    )
    test_token(expired_token, "Token scaduto")

    # 3. Token con algoritmo 'none' (NON firmato)
    none_token = jwt.encode(
        {"sub": USERNAME}, key=None, algorithm=None
    )
    test_token(none_token, "Token con alg=none (non firmato)")

    # 4. Token con claim falsificato (issuer sbagliato)
    forged_token = jwt.encode(
        {
            "sub": USERNAME,
            "iss": "attacker.example.com",
            "exp": datetime.datetime.utcnow() + datetime.timedelta(minutes=5),
        },
        SECRET_KEY,
        algorithm="HS512"
    )
    test_token(forged_token, "Token con issuer falsificato")

if __name__ == "__main__":
    main()
