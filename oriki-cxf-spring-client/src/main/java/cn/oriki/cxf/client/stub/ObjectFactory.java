
package cn.oriki.cxf.client.stub;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.oriki.cxf.client.stub package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetAddressByMobileNo_QNAME = new QName("http://service.cxf.oriki.cn/", "getAddressByMobileNo");
    private final static QName _GetAddressByMobileNoResponse_QNAME = new QName("http://service.cxf.oriki.cn/", "getAddressByMobileNoResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.oriki.cxf.client.stub
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetAddressByMobileNo }
     * 
     */
    public GetAddressByMobileNo createGetAddressByMobileNo() {
        return new GetAddressByMobileNo();
    }

    /**
     * Create an instance of {@link GetAddressByMobileNoResponse }
     * 
     */
    public GetAddressByMobileNoResponse createGetAddressByMobileNoResponse() {
        return new GetAddressByMobileNoResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAddressByMobileNo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetAddressByMobileNo }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.cxf.oriki.cn/", name = "getAddressByMobileNo")
    public JAXBElement<GetAddressByMobileNo> createGetAddressByMobileNo(GetAddressByMobileNo value) {
        return new JAXBElement<GetAddressByMobileNo>(_GetAddressByMobileNo_QNAME, GetAddressByMobileNo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAddressByMobileNoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetAddressByMobileNoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.cxf.oriki.cn/", name = "getAddressByMobileNoResponse")
    public JAXBElement<GetAddressByMobileNoResponse> createGetAddressByMobileNoResponse(GetAddressByMobileNoResponse value) {
        return new JAXBElement<GetAddressByMobileNoResponse>(_GetAddressByMobileNoResponse_QNAME, GetAddressByMobileNoResponse.class, null, value);
    }

}
