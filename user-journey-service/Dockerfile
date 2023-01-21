FROM python:3.10-alpine3.15
WORKDIR /code
COPY requirement.txt /code
RUN pip install -r requirement.txt
COPY main.py /code
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]