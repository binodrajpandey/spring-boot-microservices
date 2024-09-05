from fastapi import FastAPI

app = FastAPI()


@app.get("/api/funnel")
async def root():
  return {"message": "Hello I am from funnel service"}