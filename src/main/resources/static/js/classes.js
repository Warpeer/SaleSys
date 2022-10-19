class personalSubscribtion {
    first_name;
    last_name;
    phone_number;
    provider;
    price;
    dataAmount;

    constructor(first_name, last_name, phone_number, provider, price, dataAmount) {
        if(first_name!==null){
            this.first_name=first_name;
        }else this.first_name=null;
        if(last_name!==null){
            this.last_name=last_name;
        }else this.last_name=null;

        this.phone_number=phone_number;
        this.provider=provider;
        this.price=price;
        this.dataAmount=dataAmount;
    }
}
class subscribtion {
    provider;
    price;
    dataAmount;
    constructor(provider, price, dataAmount) {
        this.provider = provider;
        this.price = price;
        this.dataAmount = dataAmount;
    }
}
const ourPrices = [
    new subscribtion("Ice", 149, 1),
    new subscribtion("Ice", 199, 3),
    new subscribtion("Ice", 249, 8),
    new subscribtion("Ice", 299, 16),
    new subscribtion("Ice", 349, 30),
    new subscribtion("Ice", 399, 999)
];