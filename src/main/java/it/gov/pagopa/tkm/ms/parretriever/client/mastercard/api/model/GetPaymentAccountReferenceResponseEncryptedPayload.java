/*
 * Payment Account Reference Inquiry API
 * Merchants, Acquirers, or Digital Activity Customers (DACs) can use this service to query the PAR Vault to obtain a Payment Account Reference(PAR), from a Primary Account Number (PAN). PAR provides an industry-aligned approach designed to help link PAN-based transactions to transactions using associated payment tokens, without using the PAN as the linkage mechanism.
 *
 * The version of the OpenAPI document: 1.3
 * Contact: apisupport@mastercard.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

/**
 * GetPaymentAccountReferenceResponseEncryptedPayload
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-04-15T11:20:33.252+02:00[Europe/Berlin]")
public class GetPaymentAccountReferenceResponseEncryptedPayload {
  public static final String SERIALIZED_NAME_ENCRYPTED_DATA = "encryptedData";
  @SerializedName(SERIALIZED_NAME_ENCRYPTED_DATA)
  private GetPaymentAccountReferenceResponseEncryptedPayloadEncryptedData encryptedData;


  public GetPaymentAccountReferenceResponseEncryptedPayload encryptedData(GetPaymentAccountReferenceResponseEncryptedPayloadEncryptedData encryptedData) {
    
    this.encryptedData = encryptedData;
    return this;
  }

   /**
   * Get encryptedData
   * @return encryptedData
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public GetPaymentAccountReferenceResponseEncryptedPayloadEncryptedData getEncryptedData() {
    return encryptedData;
  }


  public void setEncryptedData(GetPaymentAccountReferenceResponseEncryptedPayloadEncryptedData encryptedData) {
    this.encryptedData = encryptedData;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPaymentAccountReferenceResponseEncryptedPayload getPaymentAccountReferenceResponseEncryptedPayload = (GetPaymentAccountReferenceResponseEncryptedPayload) o;
    return Objects.equals(this.encryptedData, getPaymentAccountReferenceResponseEncryptedPayload.encryptedData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(encryptedData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPaymentAccountReferenceResponseEncryptedPayload {\n");
    sb.append("    encryptedData: ").append(toIndentedString(encryptedData)).append("\n");
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

