package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

/**
 * GetPaymentAccountReferenceRequestEncryptedPayloadEncryptedData
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-04-15T11:20:33.252+02:00[Europe/Berlin]")
public class GetPaymentAccountReferenceRequestEncryptedPayloadEncryptedData {
    public static final String SERIALIZED_NAME_ACCOUNT_NUMBER = "accountNumber";
    @SerializedName(SERIALIZED_NAME_ACCOUNT_NUMBER)
    private String accountNumber;

    public static final String SERIALIZED_NAME_DATA_VALID_UNTIL_TIMESTAMP = "dataValidUntilTimestamp";
    @SerializedName(SERIALIZED_NAME_DATA_VALID_UNTIL_TIMESTAMP)
    private String dataValidUntilTimestamp;


    public GetPaymentAccountReferenceRequestEncryptedPayloadEncryptedData accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    /**
     * The PAR assigned to the PAN.&lt;/br&gt; 
     * @return accountNumber
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(example = "5123456789012345", value = "The Primary Account Number of the account or the affiliated MDES Token.</br> ")

    public String getAccountNumber() {
        return accountNumber;
    }


    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public GetPaymentAccountReferenceRequestEncryptedPayloadEncryptedData dataValidUntilTimestamp(String dataValidUntilTimestamp) {

        this.dataValidUntilTimestamp = dataValidUntilTimestamp;
        return this;
    }

    /**
     * The date/time after which this encrypted payload object is considered invalid. If present, all systems must reject this encrypted object after this time and treat it as invalid data. Must be expressed in ISO 8601 extended format as one of the following - YYYY-MM-DDThh:mm:ss[ .sss ]Z, YYYY-MM-DDThh:mm:ss[ .sss ]±hh:mm . Where [ .sss ] is optional and can be 1 to 3 digits. Must be a value no more than 30 days in the future. Mastercard recommends using a value of (Current Time + 30 minutes).
     * @return dataValidUntilTimestamp
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(example = "2021-07-04T12:09:56.123-07:00", value = "The date/time after which this encrypted payload object is considered invalid. If present, all systems must reject this encrypted object after this time and treat it as invalid data. Must be expressed in ISO 8601 extended format as one of the following - YYYY-MM-DDThh:mm:ss[ .sss ]Z, YYYY-MM-DDThh:mm:ss[ .sss ]±hh:mm . Where [ .sss ] is optional and can be 1 to 3 digits. Must be a value no more than 30 days in the future. Mastercard recommends using a value of (Current Time + 30 minutes).")

    public String getDataValidUntilTimestamp() {
        return dataValidUntilTimestamp;
    }


    public void setDataValidUntilTimestamp(String dataValidUntilTimestamp) {
        this.dataValidUntilTimestamp = dataValidUntilTimestamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetPaymentAccountReferenceRequestEncryptedPayloadEncryptedData GetPaymentAccountReferenceRequestEncryptedPayloadEncryptedData = (GetPaymentAccountReferenceRequestEncryptedPayloadEncryptedData) o;
        return Objects.equals(this.accountNumber, GetPaymentAccountReferenceRequestEncryptedPayloadEncryptedData.accountNumber) &&
                Objects.equals(this.dataValidUntilTimestamp, GetPaymentAccountReferenceRequestEncryptedPayloadEncryptedData.dataValidUntilTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, dataValidUntilTimestamp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GetPaymentAccountReferenceRequestEncryptedPayloadEncryptedData {\n");
        sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
        sb.append("    dataValidUntilTimestamp: ").append(toIndentedString(dataValidUntilTimestamp)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
