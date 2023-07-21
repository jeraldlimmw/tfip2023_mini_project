package telegram.billbot.constants;

public class Information {
    public static final String START_MESSAGE = """
        Hi there! 
        
        If you'd like to split a new bill, click on the button below and you will redirected to our webpage. After completing the split, this chat will be notified of how to best settle the bill. Enjoy!
    """;

    public static final String HELP_INFO = """
        Billbuddy was created to help you split up complicated bills so you and your friends can easily settle up.

        Simply begin by using the start command. Hit the button that is in the start message and be redirected to the billbuddy site. From there, simply carry out these three steps to obtain a set of optimised instructions to split your bill:

        Step 1: Add your friends and expenditure
        Step 2: Add the items in your bill
        Step 3: Split the bill accoridng to shares or percentage

        To begin splitting a bill, type /start
    """;
}
