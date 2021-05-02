package banking;

public class Luhn {

    public static char getLuhnDigit(String cardNumber){
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {

            int digit = Integer.parseInt(cardNumber.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        int mod = sum % 10;
        int result = (mod == 0) ? 0 : 10 - mod;
        return Character.forDigit(result, 10);
    }


    static boolean checkSum(String cardNo)
    {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;


            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }



}


