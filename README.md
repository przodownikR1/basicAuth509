# basicAuth509



 keytool -genkey -alias x509 -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650 
Enter keystore password:  
What is your first and last name?
  [Unknown]:  Borowiec
What is the name of your organizational unit?
  [Unknown]:  scalatech
What is the name of your organization?
  [Unknown]:  scalatech
What is the name of your City or Locality?
  [Unknown]:  Warsaw
What is the name of your State or Province?
  [Unknown]:  mazowieckie
What is the two-letter country code for this unit?
  [Unknown]:  PL
Is CN=Borowiec, OU=scalatech, O=scalatech, L=Warsaw, ST=mazowieckie, C=PL correct?
  [no]:  yes

  password=slawek123
  
  
  Approach 2
  
Files :   application.properties  gc.log.0.current  keystore.p12  static/  templates/
przodownik ~/$ openssl genrsa -out private.key 1024
Generating RSA private key, 1024 bit long modulus
.++++++
...............................++++++
e is 65537 (0x10001)
przodownik ~/$ openssl req -new -x509 -days 365 -key private.key -out certificate.crt

Country Name (2 letter code) [AU]:PL
State or Province Name (full name) [Some-State]:przodownik
Locality Name (eg, city) []:Warsaw
Organization Name (eg, company) [Internet Widgits Pty Ltd]:scalatech
Organizational Unit Name (eg, section) []:design
Common Name (e.g. server FQDN or YOUR name) []:
Email Address []:przodownikR1@gmail.com
przodownik ~/$ ls
application.properties  certificate.crt  gc.log.0.current  keystore.p12  private.key  static/  templates/
przodownik ~/$ 
keytool -importcert -alias x509 -keystore tomcar-ssl.truststore -file certificate.crt 
Enter keystore password:  ⏎                 
przodownik ~/)$ 
keytool -importcert -alias x509 -keystore tomcat-ssl.truststore -file certificate.crt 
Enter keystore password:  
Re-enter new password: 
Owner: EMAILADDRESS=przodownikR1@gmail.com, OU=design, O=scalatech, L=Warsaw, ST=przodownik, C=PL
Issuer: EMAILADDRESS=przodownikR1@gmail.com, OU=design, O=scalatech, L=Warsaw, ST=przodownik, C=PL
Serial number: cf03ac01882ed9e5
Valid from: Mon Dec 07 00:58:11 CET 2015 until: Tue Dec 06 00:58:11 CET 2016
Certificate fingerprints:
     MD5:  91:B4:CB:09:CD:46:E1:F7:EE:B9:2F:32:56:04:D4:19
     SHA1: DF:F2:35:32:98:EC:4B:B4:13:D5:D2:3C:FA:99:DA:E7:57:C0:52:36
     SHA256: FB:41:B1:F7:97:8B:D6:5E:73:54:8D:54:F6:E2:C6:34:B7:94:39:86:58:C6:3E:90:E0:D9:62:36:34:09:01:00
     Signature algorithm name: SHA256withRSA
     Version: 3

Extensions: 

#1: ObjectId: 2.5.29.35 Criticality=false
AuthorityKeyIdentifier [
KeyIdentifier [
0000: 83 EA 30 4C E6 A2 D3 2C   62 B7 B3 08 2A B5 14 D4  ..0L...,b...*...
0010: 90 99 30 BB                                        ..0.
]
]

#2: ObjectId: 2.5.29.19 Criticality=false
BasicConstraints:[
  CA:true
  PathLen:2147483647
]

#3: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 83 EA 30 4C E6 A2 D3 2C   62 B7 B3 08 2A B5 14 D4  ..0L...,b...*...
0010: 90 99 30 BB                                        ..0.
]
]

Trust this certificate? [no]:  yes
Certificate was added to keystore
przodownik ~/$ ls
application.properties  gc.log.0.current  private.key  templates/
certificate.crt         keystore.p12      static/      tomcat-ssl.truststore
  
  
  =========================
  @Bean
    @Description("tomcat running ssl with a self-signed cert")
    public EmbeddedServletContainerCustomizer containerCustomizer()  throws FileNotFoundException {
        final String absoluteKeystoreFile = ResourceUtils.getFile(keystoreFile).getAbsolutePath();

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(  ConfigurableEmbeddedServletContainer factory) {
                if (factory instanceof TomcatEmbeddedServletContainerFactory) {
                    TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) factory;
                    containerFactory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
                            @Override
                            public void customize(Connector connector) {
                                connector.setPort(8443);
                                connector.setSecure(true);
                                connector.setScheme("https");
                                Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
                                proto.setSSLEnabled(true);
                                proto.setKeystoreFile(absoluteKeystoreFile);
                                proto.setKeystorePass(keystorePass);
                                proto.setKeystoreType("PKCS12");
                                proto.setKeyAlias("x509");
                            }
                        });
                }
            }
        };
    }
}  
=====================
 $ curl "https://$username:$password@myhost/resource"
 curl -k --cert  przodownik.pem:password https://localhost:9123/api/hello