FROM python:3.12-slim

WORKDIR /app

# Install dependencies
RUN pip install --upgrade pip
RUN pip install fastapi uvicorn torch transformers huggingface-hub

# Copy application code
COPY src/ .

# Expose the port FastAPI will run on
EXPOSE 8000

# Start the FastAPI application using uvicorn
CMD ["python", "-m", "uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]