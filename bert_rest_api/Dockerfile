FROM nvidia/cuda:12.1.1-runtime-ubuntu20.04

RUN apt-get update && \
    apt-get install -y python3-pip python3-dev python-is-python3 && \
    rm -rf /var/lib/apt/lists/*

RUN pip install --upgrade pip
RUN pip install fastapi uvicorn torch transformers huggingface-hub

WORKDIR /app

# Copy application code
COPY src/ .

# Expose the port FastAPI will run on
EXPOSE 8000

# Start the FastAPI application using uvicorn
CMD ["python", "-m", "uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]