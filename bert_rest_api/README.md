# BERT spam classifier 
Model is available here: [ivanbalaksha/bert-base-uncased-finetuned-spam-detection](https://huggingface.co/ivanbalaksha/bert-base-uncased-finetuned-spam-detection)
Dataset used for training: [ivanbalaksha/antispambot](https://huggingface.co/datasets/ivanbalaksha/antispambot)

## Build docker image
`docker build -t bert-spam-classifier .`

## Start container
`docker run -p 8000:8000 -e HG_TOKEN=<PUT_READ_ONLY_TOKEN_HERE>  bert-spam-classifier`

## Add endpoint to bot configuration
Add `remoteFilterEndpoint` to `config.yaml`