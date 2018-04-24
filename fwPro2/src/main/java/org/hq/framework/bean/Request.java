package org.hq.framework.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Request {
    public String getRequestMethod() {
        return requestMethod;
    }


    public String getRequestPath() {
        return requestPath;

    }

    private String requestMethod;
    private String requestPath;
    public Request(String requestMethod, String requestPath){
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }
/*hashcode equals*/
@Override
public boolean equals(Object o)
{
    /*if(this == o)
    {
        return true;
    }
    if(!(o instanceof PhoneNumber))
    {
        return false;
    }
    PhoneNumber pn = (PhoneNumber)o;
    return pn.prefix == prefix && pn.phoneNumber == phoneNumber;*/
    return EqualsBuilder.reflectionEquals(this, o);
}

    @Override
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this
        );
        /*int result = 17;
        result = 31 * result + prefix;
        result = 31 * result + phoneNumber;
        return result;*/
    }

}
