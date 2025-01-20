//package com.fortune.app.initializer;
//
//import com.fortune.app.card.entity.Card;
//import com.fortune.app.card.entity.CardInterpretation;
//import com.fortune.app.card.entity.Fortune;
//import com.fortune.app.enums.Orientation;
//import com.fortune.app.card.repostiory.CardInterpretationRepository;
//import com.fortune.app.card.repostiory.CardRepository;
//import com.fortune.app.card.repostiory.FortuneRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    private final FortuneRepository fortuneRepository;
//    private final CardRepository cardRepository;
//    private final CardInterpretationRepository cardInterpretationRepository;
//
//    public DataInitializer(FortuneRepository fortuneRepository, CardRepository cardRepository, CardInterpretationRepository cardInterpretationRepository) {
//        this.fortuneRepository = fortuneRepository;
//        this.cardRepository = cardRepository;
//        this.cardInterpretationRepository = cardInterpretationRepository;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (cardRepository.count() <= 0) {
//            initializeCards();
//        }
//
//        if (fortuneRepository.count() <= 0) {
//            initializeFortunes();
//        }
//
//        if (cardInterpretationRepository.count() <= 0) {
//           initializeCardInterpretations();
//        }
//    }
//
//    private void initializeFortunes() {
//        String[] fortunes={"운세", "사랑", "커리어","금전", "건강"};
//         for(String fortune : fortunes){
//             Fortune fortuneEntity = Fortune.builder()
//                     .type(fortune)
//                     .build();
//             fortuneRepository.save(fortuneEntity);
//         }
//    }
//
//    private void initializeCards() {
//        String[] majorArcana = {
//                "The Fool", "The Magician", "The High Priestess", "The Empress", "The Emperor",
//                "The Hierophant", "The Lovers", "The Chariot", "Strength", "The Hermit",
//                "Wheel of Fortune", "Justice", "The Hanged Man", "Death", "Temperance",
//                "The Devil", "The Tower", "The Star", "The Moon", "The Sun", "Judgment", "The World"
//        };
//
//        for (String name : majorArcana) {
//            Card upright = Card.builder()
//                    .name(name)
//                    .orientation(Orientation.UPRIGHT)
//                    .build();
//            Card reversed = Card.builder()
//                    .name(name)
//                    .orientation(Orientation.REVERSED)
//                    .build();
//
//            cardRepository.save(upright);
//            cardRepository.save(reversed);
//        }
//
//        String[] suits = {"Cups", "Wands", "Swords", "Pentacles"};
//        String[] values = {
//                "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10",
//                "Page", "Knight", "Queen", "King"
//        };
//
//        for (String suit : suits) {
//            for (String value : values) {
//                String cardName = value + " of " + suit;
//                Card upright = Card.builder()
//                        .name(cardName)
//                        .orientation(Orientation.UPRIGHT)
//                        .build();
//                Card reversed = Card.builder()
//                        .name(cardName)
//                        .orientation(Orientation.REVERSED)
//                        .build();
//
//                cardRepository.save(upright);
//                cardRepository.save(reversed);
//            }
//        }
//    }
//
//    private void initializeCardInterpretations() {
//        List<Fortune> fortuneEntities = fortuneRepository.findAll();
//        for (Card card : cardRepository.findAll()) {
//            for (Fortune fortune : fortuneEntities) {
//                String interpretationContent = generateInterpretation(card.getName(), fortune.getType());
//
//                CardInterpretation interpretation = CardInterpretation.builder()
//                        .card(card)
//                        .fortune(fortune)
//                        .interpretationContent(interpretationContent)
//                        .build();
//
//                cardInterpretationRepository.save(interpretation);
//            }
//        }
//    }
//
//    private String generateInterpretation(String cardName, String fortuneName) {
//        return cardName + "의 " + fortuneName + " 해석";
//    }
//}
