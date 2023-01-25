from fastapi import FastAPI

app = FastAPI()


@app.get("/api/journey")
async def root():
  return {"message": "Hello I am from journey service"}