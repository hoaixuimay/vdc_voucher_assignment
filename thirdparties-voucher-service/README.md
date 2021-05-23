# Getting Started

##Compile and run
./mvnw clean package
java -jar target/___package___.jar
## Call service
#### No delay
curl 'localhost:8080/voucher?clientId=abc'

{voucherCode: "123456789123123"}
#### Delay: return after amount of delay time
curl 'localhost:8080/voucher?clientId=abc&delayInSeconds=100'

{voucherCode: "123456789123123"}

