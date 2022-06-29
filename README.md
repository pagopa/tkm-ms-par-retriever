# Par Retriever
The batch retrieves the pans for all the parless cards saved in S2 DB, calling the service relative to its circuit

## Configuration
The follow ENVIRONMENT variables need to deploy and run the application.

- **SERVER_PORT** *The spring boot port. Default value 8080*
- **AZURE_KEYVAULT_PROFILE** *The prefix used to search for keys in the key vault (local/sit/uat/prod)*
- **AZURE_KEYVAULT_CLIENT_ID** *Azure Kevault authentication client id*
- **AZURE_KEYVAULT_CLIENT_KEY** *Azure Kevault authentication client key*
- **AZURE_KEYVAULT_TENANT_ID** *Azure Kevault authentication tenant id*
- **AZURE_KEYVAULT_URI** *Azure Kevault address*
- **KAFKA_MAX_NUMBER_OF_THREADS** *Number of threads in which parless cards will be distributed*
- **AZURE_STORAGE_ENDPOINT** *Endpoint where blob storage connects*
- **BLOB_STORAGE_NAME** *Name of blob storage where resources are located*
- **MAX_NUMBER_OF_THREADS** *Number of threads in which parless cards are divided*
- **MAX_NUMBER_OF_CARDS** *Number of parless cards got from S2 service*
- **CONSENT_MANAGER_URL** *Consent manager url localhost:8080*
- **CARD_MANAGER_URL** *Card manager url localhost:8080*
- **AMEX_URL** *URL of Amex circuit service*
- **VISA_URL_PAR** *URL of Visa circuit service*
- **MASTERCARD_URL** *URL of Mastercard circuit service*
- **KAFKA_READ_QUEUE_TOPIC** *Topic name of read queue*
- **KAFKA_SECURITY_PROTOCOL** *Way to manage queue informations*

### Develop enviroment configuration
- Set **-Dspring.profiles.active=local** as jvm setting
- Add as enviroment variable **AZURE_KEYVAULT_CLIENT_ID=~~VALUE_TO_ADD~~;AZURE_KEYVAULT_CLIENT_KEY=~~VALUE_TO_ADD~~;AZURE_KEYVAULT_TENANT_ID=~~VALUE_TO_ADD~~;AZURE_KEYVAULT_URI=~~VALUE_TO_ADD~~**

## How to start SIT azure pipeline

1. Move into:
> develop

1. Run:<br>
   `$version=??` for poweshell or `version=??` for gitbash<br>
   `mvn --batch-mode release:clean release:prepare`<br>
   `git checkout -b tmp/${version} par-retriever-${version}`<br>
   `git push --set-upstream origin tmp/${version}`<br>

2. Merge **tmp/${version}** into **release/sit**

## How to start UAT azure pipeline

1. Merge **release/sit** into **release/uat**

## How to start PROD azure pipeline

1. Merge **release/uat** into **master**