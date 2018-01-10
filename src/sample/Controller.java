package sample;


import Razbor.Sentence;
import Razbor.Token;
import Razbor.TokenType;
import Razbor.Tokeniser;
import javafx.event.ActionEvent;

import java.util.LinkedList;

public class Controller {
    Tokeniser tokeniser = new Tokeniser();
    LinkedList<Token> mainList;
    LinkedList<Token> tempSentence;
    Sentence sentence;

    public void OnClick(ActionEvent actionEvent) {
        mainList = tokeniser.getData("Летний вечер, сумерки.\n" +
                "   Торговый центр американского города,  где  не  менее  четырехсот  тысяч\n" +
                "жителей, высокие здания, стены... Когда-нибудь, пожалуй,  станет  казаться\n" +
                "невероятным, что существовали такие города.\n" +
                "   И на широкой улице, теперь почти  затихшей,  группа  в  шесть  человек:\n" +
                "мужчина лет пятидесяти,  коротенький,  толстый,  с  густой  гривой  волос,\n" +
                "выбивающихся из-под круглой черной фетровой  шляпы,  -  весьма  невзрачная\n" +
                "личность; на ремне, перекинутом через плечо,  небольшой  органчик,  какими\n" +
                "обычно пользуются уличные проповедники и певцы. С ним женщина, лет на пять\n" +
                "моложе его, не  такая  полная,  крепко  сбитая,  одетая  очень  просто,  с\n" +
                "некрасивым, но не уродливым лицом; она ведёт за руку мальчика лет  семи  и\n" +
                "несёт Библию и книжечки псалмов. Вслед  за  ними,  немного  поодаль,  идут\n" +
                "девочка лет пятнадцати, мальчик двенадцати и еще девочка лет  девяти;  все\n" +
                "они послушно, но, по-видимому, без особой охоты следуют за старшими.");
        sentence = new Sentence(mainList);
//        for (int i = 0; i < mainList.size(); i++) {
//            tempSentence = new LinkedList<>();
//            for (int j = 0; j <  mainList.size(); j++) {
//                tempSentence.add(mainList.get(j));
//                sentence = new Sentence(tempSentence);
//            }
//            break;
//        }
    }


}
