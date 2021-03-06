package menu.observers;

import menu.interfaces.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class SMSNotificationSender implements Observer {
    List<String> subscribedPhoneNumberList;
    final String CREATE_MENU_MESSAGE_FORMAT = "새로운 메뉴 %s이(가) 추가되었습니다. %d 원으로 신메뉴를 만나보세요.\n";
    final String MODIFY_MENU_NAME_MESSAGE_FORMAT = "메뉴 %s이(가) %s로 변경되었습니다.\n";
    final String MODIFY_MENU_PRICE_INCREASE_MESSAGE_FORMAT = "메뉴 %s의 가격이 %d원에서 %d원으로 증가하였습니다...\n";
    final String MODIFY_MENU_PRICE_DECREASE_MESSAGE_FORMAT = "메뉴 %s의 가격이 %d원에서 %d원으로 감소하였습니다!\n";
    final String MODIFY_MENU_ALL_MESSAGE_FORMAT = "메뉴 %s이(가) 새로이 %s로 %d원에 판매됩니다.\n";
    final String REMOVE_MENU_MESSAGE_FORMAT = "메뉴 %s가 삭제되었습니다...\n";

    public SMSNotificationSender() {
        subscribedPhoneNumberList = new LinkedList<String>();
        // TODO: need setting code for GUI if need
        System.out.println("SMS 수신 번호를 입력하세요(-1을 눌러 종료합니다.): ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print(" - ");
                String phone_number = reader.readLine();
                if (phone_number.equals("-1")) {
                    break;
                } else {
                    subscribedPhoneNumberList.add(phone_number);
                }
            }
        } catch (IOException e) {
            System.out.println("입력 중 문제가 발생했습니다.");
        }
    }

    /**
     * 문자 구독 서비스에 사용자를 추가하는 메소드.
     *
     * @param phoneNumber 추가할 사용자의 휴대폰 번호
     */
    public void subscribe(String phoneNumber) {
        subscribedPhoneNumberList.add(phoneNumber);
    }

    /**
     * 문자 구독 서비스에서 사용자를 해지하는 메소드.
     *
     * @param phoneNumber 해지할 사용자의 휴대폰 번호
     */
    public void unsubscribe(String phoneNumber) {
        subscribedPhoneNumberList.remove(phoneNumber);
    }

    @Override
    public void create(String name, int price) {
        System.out.println("Observer Detected create signal -------");
        sendMessageToSubscribedUser(new Object[]{name, price}, CREATE_MENU_MESSAGE_FORMAT);
        System.out.println("Done sending SMS to subscribers.");
    }

    @Override
    public void modify(String lastName, int lastPrice, String name, int price) {
        System.out.println("Observer Detected modify signal -------");
        // Is it changed all?
        if (!lastName.equals(name) && lastPrice != price) {
            sendMessageToSubscribedUser(new Object[]{lastName, name, price}, MODIFY_MENU_ALL_MESSAGE_FORMAT);
        }
        // Is it a name change?
        else if (!lastName.equals(name)) {
            sendMessageToSubscribedUser(new Object[]{lastName, name}, MODIFY_MENU_NAME_MESSAGE_FORMAT);
        }
        // Is it a price increase?
        else if (lastPrice < price) {
            sendMessageToSubscribedUser(new Object[]{name, lastPrice, price}, MODIFY_MENU_PRICE_INCREASE_MESSAGE_FORMAT);
        }
        // Is it a price decrease?
        else if (lastPrice > price) {
            sendMessageToSubscribedUser(new Object[]{name, lastPrice, price}, MODIFY_MENU_PRICE_DECREASE_MESSAGE_FORMAT);
        }

        System.out.println("Done sending SMS to subscribers.");
    }

    @Override
    public void delete(String name) {
        System.out.println("Observer Detected delete signal -------");
        // Create array to send arguments;
        sendMessageToSubscribedUser(new Object[]{name}, REMOVE_MENU_MESSAGE_FORMAT);
        System.out.println("Done sending SMS to subscribers.");
    }

    private void sendMessageToSubscribedUser(Object[] messageElement, String messageFormat) {
        subscribedPhoneNumberList.forEach(phoneNumber -> {
            // 메시지를 전송 하는 부분.
            // 여기서는 샘플 프로젝트이기 때문에 Console output으로 메시지를 전송하는 것처럼 보이게 합니다.
            // 실제 프로젝트에서는 메시지를 실제로 전송하는 코드를 작성하면 됩니다.
            // 사용자 정보를 프린트
            System.out.printf("Send SMS to %s : ", phoneNumber);
            // 전송할 메시지 출력
            System.out.printf(messageFormat, messageElement);
        });
    }
}
