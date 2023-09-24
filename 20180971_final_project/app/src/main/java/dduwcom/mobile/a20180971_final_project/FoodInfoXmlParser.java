package dduwcom.mobile.a20180971_final_project;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class FoodInfoXmlParser {

    private enum TagType { NONE, NUTR_CONT1, DESC_KOR };

    private final static String FAULT_RESULT = "faultResult";
    private final static String ROW_TAG = "row";
    private final static String NUTR_CONT1_TAG = "NUTR_CONT1"; //칼로리
    private final static String DESC_KOR_TAG = "DESC_KOR"; // 메뉴명

    private XmlPullParser parser;

    //파서 생성
    public FoodInfoXmlParser() {

        XmlPullParserFactory factory = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }


    public List<FoodInfo> parse(String xml) {
        List<FoodInfo> resultList = new ArrayList();
        FoodInfo dbo = null;
        TagType tagType = TagType.NONE;     //  태그를 구분하기 위한 enum 변수 초기화

        try {

            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();      // 태그 유형 구분 변수 준비

            while (eventType != XmlPullParser.END_DOCUMENT) {  // parsing 수행
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if (tag.equals(ROW_TAG)) {    // 새로운 항목을 표현하는 태그를 만났을 경우 dto 객체 생성
                            dbo = new FoodInfo();
                        } else if (tag.equals(NUTR_CONT1_TAG)) {
                            if (dbo != null) tagType = TagType.NUTR_CONT1;
                        } else if (tag.equals(DESC_KOR_TAG)) {
                            tagType = TagType.DESC_KOR;
                        } else if (tag.equals(FAULT_RESULT)) {
                            return null;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(ROW_TAG)) {
                            resultList.add(dbo);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {       // 태그의 유형에 따라 dto 에 값 저장
                            case NUTR_CONT1:
                                dbo.setNUTR_CONT1(Double.parseDouble(parser.getText()));
                                break;
                            case DESC_KOR:
                                dbo.setDESC_KOR(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
