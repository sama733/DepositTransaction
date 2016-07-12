package serverSide;

import java.io.Serializable;
import java.math.BigDecimal;

public class Response implements Serializable {
    private String id;
    private ResponseType responseType;
    private BigDecimal newBalance;
    private String description;

    public Response() {

    }

    public Response(String id, ResponseType responseType, BigDecimal newBalance, String description) {
        this.id = id;
        this.responseType = responseType;
        this.newBalance = newBalance;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id='" + id + '\'' +
                ", responseType=" + responseType +
                ", newBalance=" + newBalance +
                ", description='" + description + '\'' +
                '}';
    }
}

