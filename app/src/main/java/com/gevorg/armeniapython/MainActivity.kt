package com.gevorg.armeniapython

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.EmojiEvents

private val Ink = Color(0xFF182A42)
private val Blue = Color(0xFF315BE8)
private val PaleBlue = Color(0xFFEAF0FF)
private val Page = Color(0xFFF6F8FC)
private val Green = Color(0xFF18794E)
private val Red = Color(0xFFB42318)

private data class Question(
    val prompt: String,
    val choices: List<String>,
    val answer: Int,
    val explanation: String
)

private data class Lesson(
    val title: String,
    val subtitle: String,
    val explanation: String,
    val code: String,
    val takeaway: String,
    val questions: List<Question>
)

private val lessons = listOf(
    Lesson("1. Առաջին քայլերը Python-ում", "Ծանոթացիր ծրագրին ու առաջին հրամանին։",
        "Python-ը ծրագրավորման լեզու է․ մենք գրում ենք պարզ հրահանգներ, իսկ Python-ը կատարում է դրանք։ Կոդը տեքստային ֆայլում գրում ենք .py վերջավորությամբ։ print-ը ներկառուցված հրաման է, որը արժեքը ցույց է տալիս էկրանին։ Փակագծերի ներսում դնում ենք այն, ինչ ուզում ենք տպել։ Տեքստը պետք է լինի չակերտների մեջ։",
        "print(\"Բարև, աշխարհ։\")", "print-ը ցույց է տալիս արդյունքը։ Տեքստը գրիր չակերտների մեջ։", listOf(
            Question("Ի՞նչ է անում print(\"Ողջույն\") հրամանը։", listOf("Ցույց է տալիս Ողջույն տեքստը", "Ջնջում է ծրագիրը", "Ստեղծում է ֆայլ"), 0, "print-ը էկրանին արտածում է փակագծերի ներսի արժեքը։"),
            Question("Տեքստն ինչպե՞ս ենք գրում Python կոդում։", listOf("Չակերտների ներսում", "Միշտ քառակուսի փակագծերում", "Առանց նշանների"), 0, "Տեքստային արժեքը՝ string-ը, դրվում է մեկ կամ կրկնակի չակերտների մեջ։")
        )),
    Lesson("2. Փոփոխականներ և արժեքների տեսակներ", "Պահիր տվյալներ անունների տակ։",
        "Փոփոխականը անունով պիտակ է, որի միջոցով ծրագիրը հիշում է արժեքը։ = նշանը նշանակում է՝ աջ կողմի արժեքը վերագրել ձախ կողմի անունին։ Օրինակ՝ age = 20։ Հիմնական տեսակներն են int՝ ամբողջ թիվ, float՝ տասնորդական թիվ, str՝ տեքստ, bool՝ ճիշտ կամ սխալ արժեք։ Python-ը սովորաբար ինքն է որոշում տեսակը։",
        "name = \"Անի\"\nage = 20\nheight = 1.68\nis_learning = True\nprint(name, age)", "Փոփոխականի անունը ձախում է, արժեքը՝ = նշանի աջ կողմում։", listOf(
            Question("Ո՞ր տողն է պահում ամբողջ թիվ։", listOf("count = 7", "count = \"յոթ\"", "count = 2.5"), 0, "7-ն առանց չակերտների ամբողջ թիվ է՝ int։ Չակերտների մեջ այն տեքստ կլիներ։"),
            Question("Ի՞նչ արժեքներ կարող է ունենալ bool տեսակը։", listOf("True կամ False", "Միայն տեքստ", "Ցանկացած տասնորդական թիվ"), 0, "bool-ը տրամաբանական տեսակ է․ այն ներկայացնում է երկու վիճակ՝ True և False։")
        )),
    Lesson("3. Մուտքագրում և փոխակերպում", "Ծրագրից հարցրու օգտվողին։",
        "input() հրամանը սպասում է, մինչև մարդը մուտքագրի պատասխան։ Կարևոր է․ input()-ի արդյունքը միշտ տեքստ է, նույնիսկ եթե մարդը թվանշան է գրել։ Եթե պետք է հաշվել, տեքստը փոխակերպիր int()-ով կամ float()-ով։ Փոխակերպումը հնարավոր է միայն այն դեպքում, երբ տեքստը համապատասխան թիվ է պարունակում։",
        "name = input(\"Անունդ՝ \")\nage = int(input(\"Տարիքդ՝ \"))\nprint(\"Բարև,\", name)\nprint(\"Հաջորդ տարի կլինես\", age + 1)", "input-ը տեքստ է վերադարձնում․ թվային հաշվարկից առաջ փոխակերպիր։", listOf(
            Question("Ի՞նչ տեսակ է վերադարձնում input() ֆունկցիան։", listOf("str՝ տեքստ", "int՝ ամբողջ թիվ", "bool՝ ճիշտ/սխալ"), 0, "input()-ը միշտ վերադարձնում է str։ Թվային հաշվարկի համար պետք է int() կամ float()։"),
            Question("Ինչպե՞ս մուտքագրած \"12\" տեքստը դարձնել ամբողջ թիվ։", listOf("int(\"12\")", "str(12)", "print(12)"), 0, "int()-ը փոխակերպում է թվանշաններից կազմված տեքստը ամբողջ թվի։")
        )),
    Lesson("4. Գործողություններ և համեմատություններ", "Հաշվիր և համեմատի՛ր արժեքները։",
        "Թվերի հետ կարող ես գումարել +, հանել -, բազմապատկել *, բաժանել /։ //-ը բաժանում է առանց տասնորդական մասի, իսկ %-ը տալիս է մնացորդը։ **-ը աստիճան բարձրացնելու նշանն է։ Համեմատության նշաններն են == հավասար է, != հավասար չէ, <, >, <=, >=։ Մի շփոթիր = վերագրումը և == համեմատությունը։",
        "total = 8 + 3\nremainder = 8 % 3\nis_big = total >= 10\nprint(total, remainder, is_big)", "= նշանակում է վերագրել, իսկ ==՝ ստուգել հավասարությունը։", listOf(
            Question("Ո՞ր նշանն է ստուգում՝ երկու արժեք հավասա՞ր են։", listOf("==", "=", "%"), 0, "==-ը համեմատում է հավասարությունը։ Միայն = նշանը արժեք է վերագրում։"),
            Question("Ի՞նչ է վերադարձնում 8 % 3 արտահայտությունը։", listOf("2՝ բաժանման մնացորդը", "2.66", "24"), 0, "% օպերատորը վերադարձնում է բաժանման մնացորդը․ 8-ը 3-ի բաժանելիս մնում է 2։")
        )),
    Lesson("5. Պայմաններ՝ if / elif / else", "Ընտրիր՝ ինչ անել իրավիճակից կախված։",
        "if-ը ստուգում է պայմանը։ Եթե այն True է, կատարվում է հաջորդ տողի ներս ընկած կոդը։ Python-ում այդ ներս ընկնելը ցույց է տալիս բացատներով indentation-ը, ոչ թե փակագծերը։ elif-ը ստուգում է լրացուցիչ պայման, իսկ else-ը կատարվում է, երբ նախորդ պայմաններից ոչ մեկը ճիշտ չէ։ Համեմատության արդյունքը True կամ False է։",
        "score = 84\nif score >= 90:\n    print(\"Գերազանց\")\nelif score >= 60:\n    print(\"Անցար\")\nelse:\n    print(\"Կրկնիր նյութը\")", "if-ի մարմինը պետք է լինի ներս ընկած․ սովորաբար օգտագործիր 4 բացատ։", listOf(
            Question("Ե՞րբ է աշխատում else-ի մարմինը։", listOf("Երբ if/elif պայմաններից ոչ մեկը ճիշտ չէ", "Միշտ առաջինը", "Միայն երբ ծրագիրը սխալ է"), 0, "else-ը պահուստային ճյուղն է․ այն աշխատում է, երբ if և elif պայմանները False են։"),
            Question("Ինչպե՞ս է Python-ում նշվում if-ի ներսի կոդը։", listOf("Նահանջով՝ բացատներով", "Միայն գանգուր փակագծերով", "Մեծատառերով"), 0, "Python-ում indentation-ը կառուցվածքի մասն է։ Սովորաբար կիրառիր 4 բացատ՝ յուրաքանչյուր մակարդակի համար։")
        )),
    Lesson("6. Կրկնություններ՝ for և while", "Կրկնիր գործողությունները՝ առանց նույն կոդը կրկնելու։",
        "for ցիկլը հերթով անցնում է հավաքածուի տարրերով կամ range()-ի թվերով։ range(5)-ը տալիս է 0-ից 4 թվերը․ ավարտի սահմանը չի ներառվում։ while-ը կրկնում է, քանի դեռ պայմանը True է։ while-ի ներսում փոխիր պայմանին մասնակցող արժեքը, այլապես կարող ես ստանալ անվերջ ցիկլ։ break-ը կարող է դադարեցնել ցիկլը։",
        "for number in range(3):\n    print(number)\n\ncount = 0\nwhile count < 3:\n    count += 1", "for-ը հաճախ օգտագործիր տարրերի համար, while-ը՝ պայմանով կրկնելու համար։", listOf(
            Question("Ի՞նչ թվեր է տալիս range(3)-ը։", listOf("0, 1, 2", "1, 2, 3", "0, 1, 2, 3"), 0, "range-ի մեկնարկային արժեքը լռելյայն 0 է, իսկ ավարտի սահմանը չի ներառվում։"),
            Question("Ի՞նչը կարող է անվերջ պահել while ցիկլը։", listOf("Պայմանը երբեք չի դառնում False", "Ցիկլում կա print", "Փոփոխականը թիվ է"), 0, "Եթե ցիկլի ընթացքում պայմանը չփոխվի դեպի False, while-ը կշարունակի կրկնվել։")
        )),
    Lesson("7. Տողեր՝ տեքստի հետ աշխատանք", "Գործիր տեքստի հետ։",
        "Տողը (str) տեքստային արժեք է։ Տողի նիշերի դիրքերը՝ ինդեքսները, սկսվում են 0-ից։ text[0]-ը առաջին նիշն է, իսկ text[-1]-ը՝ վերջինը։ Կտրվածքը text[1:4] վերցնում է 1, 2, 3 ինդեքսների նիշերը՝ վերջի սահմանը չներառելով։ f-տողը՝ f\"Բարև {name}\", հարմար է արժեքները տեքստի մեջ տեղադրելու համար։",
        "language = \"Python\"\nprint(language[0])\nprint(language[-1])\nname = \"Անի\"\nprint(f\"Բարև, {name}!\")", "Ինդեքսը սկսվում է 0-ից, իսկ f-տողի արտահայտությունը գրիր { }-ի մեջ։", listOf(
            Question("Ի՞նչ կարտածի \"Python\"[0]-ը։", listOf("P", "y", "n"), 0, "Տողի ինդեքսավորումը 0-ից է սկսվում, ուրեմն ինդեքս 0-ը առաջին նիշն է՝ P։"),
            Question("Ինչպե՞ս f-տողի ներսում տեղադրել name փոփոխականի արժեքը։", listOf("{name}", "(name)", "[name]"), 0, "f-տողի ներսում Python արտահայտությունները դրվում են գանգուր փակագծերի մեջ։")
        )),
    Lesson("8. Ցուցակներ և բազմություններ", "Պահիր մի քանի արժեք մեկ տեղում։",
        "Ցուցակը (list) փոփոխվող, հերթականությամբ դասավորված արժեքների շարք է։ Ստեղծիր քառակուսի փակագծերով՝ fruits = [\"խնձոր\", \"տանձ\"]։ append()-ը տարր է ավելացնում վերջում։ Բազմությունը (set) պահում է եզակի արժեքներ և հեռացնում կրկնությունները։ Ցուցակի ինդեքսը սկսվում է 0-ից, իսկ len()-ը վերադարձնում է տարրերի քանակը։",
        "scores = [8, 10, 7]\nscores.append(9)\nprint(scores[0])\nunique_scores = set(scores)\nprint(len(unique_scores))", "list-ը պահպանում է հերթականությունը և կրկնությունները, set-ը՝ միայն եզակի արժեքները։", listOf(
            Question("Ո՞ր մեթոդն է նոր տարր ավելացնում ցուցակի վերջում։", listOf("append()", "add_end()", "push_text()"), 0, "list-ի append(արժեք) մեթոդը տարրն ավելացնում է ցուցակի վերջում։"),
            Question("Ի՞նչ է անում set([2, 2, 3])-ը։", listOf("Պահում է 2 և 3 եզակի արժեքները", "Պահում է միայն առաջին 2-ը", "Ստեղծում է տող"), 0, "set-ը պահում է միայն եզակի արժեքներ, այնպես որ կրկնվող 2-ը մեկ անգամ է մնում։")
        )),
    Lesson("9. Բառարաններ և key-value տվյալներ", "Կապիր բանալիները համապատասխան արժեքների հետ։",
        "Բառարանը (dict) տվյալները պահում է բանալի՝ արժեք զույգերով։ Օրինակ՝ user = {\"name\": \"Անի\", \"age\": 20}։ Բանալիներով արժեքը կարդում ենք user[\"name\"] ձևով։ get()-ը թույլ է տալիս ապահով կերպով կարդալ արժեքը և պահուստային արդյունք նշել, եթե բանալին չկա։ Բանալիները եզակի են։",
        "person = {\"name\": \"Արամ\", \"age\": 25}\nprint(person[\"name\"])\nperson[\"city\"] = \"Երևան\"\nprint(person.get(\"job\", \"նշված չէ\"))", "dict-ը տվյալ է փնտրում իմաստալից բանալիով, ոչ թե թվային դիրքով։", listOf(
            Question("Ինչպե՞ս կարդալ person բառարանի name արժեքը։", listOf("person[\"name\"]", "person(0)", "person.name()"), 0, "Բառարանի արժեքը հասանելի է իր բանալիով՝ քառակուսի փակագծերում։"),
            Question("Ինչո՞վ է օգտակար get(\"job\", \"չկա\")-ը։", listOf("Բացակայող բանալու համար տալիս է պահուստային արժեք", "Ջնջում է բառարանը", "Դասավորում է բանալիները"), 0, "get()-ը չի առաջացնում KeyError, եթե բանալին բացակայում է․ այն վերադարձնում է նշված լռելյայն արժեքը։")
        )),
    Lesson("10. Ֆունկցիաներ", "Կոդը բաժանիր կրկնակի օգտագործվող մասերի։",
        "Ֆունկցիան անվանված, վերօգտագործվող կոդի բլոկ է։ Սահմանելու համար օգտագործիր def, անուն և փակագծեր։ Ֆունկցիայի մարմինը պետք է ներս ընկած լինի։ Պարամետրերը ընդունում են մուտքային արժեքները։ return-ը արժեքը վերադարձնում է կանչող կոդին․ print-ը միայն ցույց է տալիս այն էկրանին։ Առանց return-ի ֆունկցիան վերադարձնում է None։",
        "def add(a, b):\n    return a + b\n\nresult = add(2, 3)\nprint(result)", "return-ը վերադարձնում է արդյունքը, իսկ print-ը ցուցադրում է այն։", listOf(
            Question("Ո՞ր հրամանն է ֆունկցիայից արժեք վերադարձնում։", listOf("return", "print", "input"), 0, "return-ը փոխանցում է արդյունքը ֆունկցիան կանչած հատվածին։ print-ը միայն արտածում է։"),
            Question("Ինչպե՞ս սահմանել Python ֆունկցիա։", listOf("def անուն(...):", "function անուն {...}", "make անուն()"), 0, "Python-ում ֆունկցիան սահմանվում է def բառով, հետո անունով և փակագծերով։")
        )),
    Lesson("11. Մոդուլներ և միջավայրեր", "Օգտագործիր ուրիշների պատրաստի կոդը։",
        "Մոդուլը Python ֆայլ է, որը կարող է պարունակել ֆունկցիաներ ու արժեքներ։ import math ձևով ներմուծում ենք ստանդարտ գրադարանի մոդուլը։ Փաթեթը մի քանի մոդուլների հավաքածու է։ Արտաքին գրադարաններն առանձին տեղադրում են pip-ով։ Իրական նախագծում մեկուսացված վիրտուալ միջավայրը (venv) օգնում է, որ նախագծերի գրադարանները չխառնվեն։",
        "import math\nprint(math.sqrt(25))\n\n# Տերմինալում՝\n# python -m venv .venv\n# python -m pip install requests", "Սկզբում ստուգիր ստանդարտ գրադարանը․ արտաքին փաթեթները տեղադրիր նախագծի միջավայրում։", listOf(
            Question("Ինչպե՞ս ներմուծել math մոդուլը։", listOf("import math", "include math", "using math"), 0, "Python-ի մոդուլ ներմուծելու ստանդարտ հրամանն է import անուն։"),
            Question("Ինչո՞ւ է պետք venv-ը։", listOf("Նախագծերի գրադարանները մեկուսացնելու համար", "Կոդը հայերեն դարձնելու համար", "Տողերը ինքնաշխատ ուղղելու համար"), 0, "Վիրտուալ միջավայրը նախագծի կախվածությունները մեկուսացնում է համակարգի և այլ նախագծերի միջավայրերից։")
        )),
    Lesson("12. Սխալներ և բացառություններ", "Կառավարիր խնդիրները՝ առանց ծրագրի անսպասելի կանգնելու։",
        "Սինտաքսային սխալը նշանակում է՝ կոդը ճիշտ գրված չէ։ Բացառությունը առաջանում է կատարման ընթացքում, օրինակ՝ թվի փոխարեն անհամապատասխան արժեք փոխարկելիս։ try բլոկում տեղադրիր այն կոդը, որը կարող է ձախողվել։ except-ում որոշիր՝ ինչպես արձագանքել։ Բռնիր հնարավորինս կոնկրետ սխալի տեսակը, ոչ թե լռեցրու բոլոր սխալները։",
        "try:\n    age = int(input(\"Տարիք՝ \"))\nexcept ValueError:\n    print(\"Մուտքագրիր ամբողջ թիվ։\")", "except-ում բռնիր այն սխալի տեսակը, որը կարող ես հասկանալ ու մշակել։", listOf(
            Question("Ո՞ր սխալն է հավանական, եթե int()-ին տանք \"բարև\"։", listOf("ValueError", "IndexError", "NameError"), 0, "ValueError է լինում, երբ արժեքը ճիշտ տեսակի է թվում, բայց հնարավոր չէ այն փոխակերպել պահանջված արժեքի։"),
            Question("Ինչի՞ համար է try/except-ը։", listOf("Բռնելու և մշակելու հնարավոր բացառությունները", "Ցիկլերը արագացնելու", "Փոփոխականներ ջնջելու"), 0, "try/except-ը թույլ է տալիս արձագանքել սպասվող կատարման սխալին՝ ծրագիրը կտրուկ կանգնեցնելու փոխարեն։")
        )),
    Lesson("13. Ֆայլեր և with", "Կարդա ու պահպանիր տվյալները։",
        "open()-ը բացում է ֆայլ։ \"r\" ռեժիմը կարդում է, \"w\"-ը գրում է՝ հին պարունակությունը փոխարինելով, իսկ \"a\"-ն ավելացնում է վերջում։ with կառուցվածքը ֆայլը փակում է ինքնաշխատ՝ նույնիսկ սխալի դեպքում։ encoding=\"utf-8\" գրիր, երբ ֆայլում հայերեն տեքստ կա։ Ֆայլի ուղին պետք է ճիշտ լինի, իսկ գրելու թույլտվություն պետք է ունենաս։",
        "with open(\"notes.txt\", \"w\", encoding=\"utf-8\") as file:\n    file.write(\"Սովորում եմ Python։\")\n\nwith open(\"notes.txt\", encoding=\"utf-8\") as file:\n    text = file.read()", "with-ը ապահովում է ֆայլի փակում, իսկ UTF-8-ը՝ հայերեն տեքստի ճիշտ պահպանում։", listOf(
            Question("Ի՞նչ ռեժիմով կարելի է ավելացնել տեքստ ֆայլի վերջում։", listOf("\"a\"", "\"r\"", "\"x\""), 0, "\"a\" ռեժիմը append է՝ ավելացնում է պարունակությունը ֆայլի վերջում։"),
            Question("Ինչո՞ւ է with open(...) հարմար։", listOf("Ֆայլը փակվում է բլոկի ավարտին", "Ավտոմատ թարգմանում է տեքստը", "Ջնջում է սխալները"), 0, "with-ը կառավարում է ռեսուրսի օգտագործման տևողությունը և ֆայլը փակում է նաև բացառության դեպքում։")
        )),
    Lesson("14. Դասեր և օբյեկտներ", "Ծանոթացիր օբյեկտակողմնորոշված ծրագրավորմանը։",
        "Դասը (class) օբյեկտների ստեղծման նկարագիր է։ Օբյեկտը այդ դասի կոնկրետ օրինակն է։ __init__ մեթոդը կանչվում է նոր օբյեկտ ստեղծելիս։ self-ը հղում է տվյալ օբյեկտին, և մեթոդի առաջին պարամետրը սովորաբար self է։ Օբյեկտի տվյալները կոչվում են ատրիբուտներ, իսկ նրա ֆունկցիաները՝ մեթոդներ։ Սկզբում ընտրիր դասեր միայն այն ժամանակ, երբ դրանք պարզեցնում են մոդելավորումը։",
        "class Dog:\n    def __init__(self, name):\n        self.name = name\n\n    def speak(self):\n        return f\"{self.name}: հաֆ\"\n\npet = Dog(\"Բոբի\")\nprint(pet.speak())", "Դասը նկարագիրն է, օբյեկտը՝ դրա հիման վրա ստեղծված կոնկրետ օրինակը։", listOf(
            Question("Ի՞նչ է class-ը։", listOf("Օբյեկտներ ստեղծելու նկարագիր", "Միայն թիվ պահելու տեղ", "Python-ի սխալների տեսակ"), 0, "Դասը սահմանում է օբյեկտի կառուցվածքն ու վարքը, իսկ օրինակները ստեղծվում են այդ նկարագրով։"),
            Question("Ի՞նչ է self-ը մեթոդում։", listOf("Տվյալ օբյեկտին հղումը", "Ֆիքսված տեքստ", "Ֆայլի անուն"), 0, "self-ը հղում է այն օբյեկտին, որի համար կանչվել է մեթոդը։")
        )),
    Lesson("15. Թեստավորում և վրիպազերծում", "Ստուգիր կոդը և գտիր պատճառը, ոչ թե գուշակիր։",
        "Թեստը ստուգում է՝ կոդը սպասված արդյունք տալի՞ս է։ assert արտահայտությունը պնդում է, որ պայմանը պետք է ճիշտ լինի․ հակառակ դեպքում այն սխալ է բարձրացնում։ Փոքր ֆունկցիաները ավելի հեշտ են թեստավորվում։ Վրիպազերծելիս կարդա սխալի հաղորդագրությունն ու traceback-ի վերջին տողերը, գտիր ֆայլն ու տողի համարը, հետո ստուգիր այդտեղի արժեքները։ Փոփոխությունն արա փոքր քայլերով և նորից գործարկիր թեստերը։",
        "def square(n):\n    return n * n\n\nassert square(3) == 9\nassert square(0) == 0", "Սպասված արդյունքը հստակ գրիր և ստուգիր փոքր, անկախ դեպքերով։", listOf(
            Question("Ի՞նչ է անում assert square(3) == 9-ը։", listOf("Ստուգում է պայմանը և սխալ է տալիս, եթե այն սխալ է", "Արտածում է 9 առանց ստուգման", "Սահմանում է ֆունկցիա"), 0, "assert-ը ստուգում է պնդումը։ Եթե այն False է, Python-ը բարձրացնում է AssertionError։"),
            Question("Որտեղի՞ց սկսել traceback-ը ուսումնասիրելիս։", listOf("Սխալի տեսակը/հաղորդագրությունն ու վերջին համապատասխան տողերը", "Միանգամից ամբողջ ծրագիրը վերագրելուց", "Պատահական տողեր ջնջելուց"), 0, "Traceback-ը տալիս է կանչերի շղթան ու վերջում սխալի տեսակը։ Սկսիր վերջին տողերից և համապատասխան կոդի վայրից։")
        )),
    Lesson("16. JSON, API-ներ և հաջորդ մակարդակը", "Փոխանակիր տվյալներ ծրագրերի միջև։",
        "JSON-ը տարածված տեքստային ձևաչափ է տվյալներ փոխանցելու համար։ Python-ի json մոդուլով dict/list արժեքները դարձնում ենք JSON տեքստ՝ dumps(), իսկ JSON տեքստը կարդում՝ loads()։ API-ն ծրագրերի միջև համաձայնեցված հաղորդակցության միջոց է․ սովորաբար այն վերադարձնում է JSON։ Ցանցային հարցումները կարող են ձախողվել, ուստի իրական ծրագրում պետք է մշակել timeout-ը, HTTP սխալները և գաղտնի բանալիները չպահել կոդի մեջ։ Դրանից հետո սովորիր SQL, Git, Linux, վեբ-ծրագրավորում, async և նախագծային ճարտարապետություն։",
        "import json\n\nuser = {\"name\": \"Անի\", \"active\": True}\ntext = json.dumps(user, ensure_ascii=False)\nrestored = json.loads(text)\nprint(restored[\"name\"])", "API-ն կապում է ծրագրերը, JSON-ը՝ փոխանցվող տվյալների ձևաչափերից մեկն է։", listOf(
            Question("Ինչպե՞ս dict-ը դարձնել JSON տեքստ։", listOf("json.dumps(data)", "json.loads(data)", "print(data)"), 0, "json.dumps()-ը Python արժեքը սերիալացնում է JSON տեքստի։ loads()-ը հակառակ գործողությունն է։"),
            Question("Ի՞նչ է API-ն այս համատեքստում։", listOf("Ծրագրերի հաղորդակցության համաձայնեցված միջերես", "Միայն Python-ի փոփոխական", "Տեքստի չակերտների տեսակ"), 0, "API-ն սահմանում է, թե ծրագրերը ինչպես կարող են հարցումներ ու պատասխաններ փոխանակել։")
        ))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(246, 248, 252)
        window.navigationBarColor = android.graphics.Color.rgb(246, 248, 252)
        window.decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        setContent { ArmenianPythonApp() }
    }
}

@Composable
private fun ArmenianPythonApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("learning_progress", android.content.Context.MODE_PRIVATE) }
    var unlocked by remember { mutableIntStateOf(prefs.getInt("unlocked_lessons", 1).coerceIn(1, lessons.size)) }
    var selected by remember { mutableIntStateOf(0) }
    var questionIndex by remember(selected) { mutableIntStateOf(0) }
    var choice by remember(selected, questionIndex) { mutableIntStateOf(-1) }
    var result by remember(selected, questionIndex) { mutableStateOf<Boolean?>(null) }
    val lesson = lessons[selected]
    val question = lesson.questions[questionIndex]
    val completedCount = unlocked - 1
    val isWide = LocalConfiguration.current.screenWidthDp >= 700

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Blue, onPrimary = Color.White, background = Page, surface = Color.White, onSurface = Ink)
    ) {
        Scaffold(containerColor = Page, contentWindowInsets = WindowInsets.safeDrawing) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
                Header(completedCount)
                if (isWide) {
                    Row(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                        LessonRail(selected, unlocked, Modifier.width(280.dp).fillMaxHeight()) { selected = it }
                        VerticalDivider(color = Color(0xFFE1E6EF))
                        LessonContent(lesson, questionIndex, question, choice, result,
                            onChoice = { choice = it },
                            onCheck = { if (choice == question.answer) result = true else result = false },
                            onRetry = { choice = -1; result = null },
                            onContinue = {
                                if (questionIndex < lesson.questions.lastIndex) questionIndex++
                                else {
                                    if (selected + 1 >= unlocked && unlocked < lessons.size) {
                                        unlocked = selected + 2
                                        prefs.edit().putInt("unlocked_lessons", unlocked).apply()
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f).fillMaxHeight())
                    }
                } else {
                    Column(Modifier.fillMaxSize()) {
                        LessonPicker(selected, unlocked) { selected = it }
                        LessonContent(lesson, questionIndex, question, choice, result,
                            onChoice = { choice = it },
                            onCheck = { if (choice == question.answer) result = true else result = false },
                            onRetry = { choice = -1; result = null },
                            onContinue = {
                                if (questionIndex < lesson.questions.lastIndex) questionIndex++
                                else if (selected + 1 >= unlocked && unlocked < lessons.size) {
                                    unlocked = selected + 2
                                    prefs.edit().putInt("unlocked_lessons", unlocked).apply()
                                }
                            },
                            modifier = Modifier.weight(1f).fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(completed: Int) {
    Column(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 20.dp, vertical = 14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.size(42.dp).background(PaleBlue, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Code, contentDescription = null, tint = Blue)
            }
            Column(Modifier.weight(1f)) {
                Text("Հայերեն Python", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Ink)
                Text("Քայլ առ քայլ՝ զրոյից դեպի հմտություն", fontSize = 13.sp, color = Color(0xFF5B6678))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color(0xFFB7791F), modifier = Modifier.size(20.dp))
                Text("$completed/${lessons.size}", fontWeight = FontWeight.SemiBold, color = Ink)
            }
        }
        Spacer(Modifier.height(12.dp))
        LinearProgressIndicator(progress = { completed.toFloat() / lessons.size }, modifier = Modifier.fillMaxWidth().height(6.dp), color = Blue, trackColor = PaleBlue)
    }
}

@Composable
private fun LessonPicker(selected: Int, unlocked: Int, onSelect: (Int) -> Unit) {
    LazyRow(Modifier.fillMaxWidth().background(Color.White).padding(vertical = 10.dp), contentPadding = PaddingValues(horizontal = 14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(lessons) { index, item ->
            val active = selected == index
            val allowed = index < unlocked
            Surface(
                modifier = Modifier.clickable(enabled = allowed) { onSelect(index) },
                shape = RoundedCornerShape(12.dp),
                color = if (active) Blue else if (allowed) PaleBlue else Color(0xFFF0F2F6),
                contentColor = if (active) Color.White else if (allowed) Ink else Color(0xFF9AA3B2)
            ) {
                Row(Modifier.padding(horizontal = 12.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    if (!allowed) Icon(Icons.Filled.Lock, null, modifier = Modifier.size(14.dp))
                    Text(item.title.substringBefore("."), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun LessonRail(selected: Int, unlocked: Int, modifier: Modifier = Modifier, onSelect: (Int) -> Unit) {
    Column(modifier.background(Color.White, RoundedCornerShape(18.dp)).padding(14.dp)) {
        Text("Դասընթացի ուղին", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Ink)
        Text("Անցիր դասերը հերթականությամբ։", fontSize = 13.sp, color = Color(0xFF697586), modifier = Modifier.padding(top = 3.dp, bottom = 10.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            itemsIndexed(lessons) { index, lesson ->
                val allowed = index < unlocked
                val active = selected == index
                Row(
                    Modifier.fillMaxWidth().background(if (active) PaleBlue else Color.Transparent, RoundedCornerShape(12.dp))
                        .clickable(enabled = allowed) { onSelect(index) }.padding(horizontal = 10.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (!allowed) Icon(Icons.Filled.Lock, null, tint = Color(0xFF9AA3B2), modifier = Modifier.size(18.dp))
                    else Icon(if (index < unlocked - 1) Icons.Filled.CheckCircle else Icons.Filled.MenuBook, null,
                        tint = if (index < unlocked - 1) Green else Blue, modifier = Modifier.size(18.dp))
                    Text(lesson.title, fontSize = 13.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                        color = if (allowed) Ink else Color(0xFF9AA3B2), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun LessonContent(
    lesson: Lesson,
    questionIndex: Int,
    question: Question,
    choice: Int,
    result: Boolean?,
    onChoice: (Int) -> Unit,
    onCheck: () -> Unit,
    onRetry: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.verticalScroll(rememberScrollState()).padding(horizontal = 4.dp, vertical = 4.dp)) {
        Column(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(20.dp)).padding(22.dp)) {
            Text("ԴԱՍ ${lesson.title.substringBefore(".")}", color = Blue, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.height(8.dp))
            Text(lesson.title.substringAfter(". "), fontSize = 26.sp, lineHeight = 33.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(lesson.subtitle, fontSize = 15.sp, color = Color(0xFF626F82), modifier = Modifier.padding(top = 7.dp))
            Spacer(Modifier.height(18.dp))
            Text(lesson.explanation, fontSize = 16.sp, lineHeight = 25.sp, color = Color(0xFF344054))
            Spacer(Modifier.height(18.dp))
            Text("Օրինակ", fontWeight = FontWeight.Bold, color = Ink, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text(lesson.code, Modifier.fillMaxWidth().background(Color(0xFF172235), RoundedCornerShape(14.dp)).padding(16.dp),
                color = Color(0xFFE3EDFF), fontFamily = FontFamily.Monospace, fontSize = 14.sp, lineHeight = 21.sp)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth().background(PaleBlue, RoundedCornerShape(12.dp)).padding(13.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.MenuBook, null, tint = Blue, modifier = Modifier.size(18.dp))
                Text(lesson.takeaway, fontSize = 14.sp, color = Ink, lineHeight = 21.sp)
            }
        }
        Spacer(Modifier.height(14.dp))
        Column(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(20.dp)).padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Ստուգիր գիտելիքդ", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Ink)
                Spacer(Modifier.weight(1f))
                Text("${questionIndex + 1}/${lesson.questions.size}", color = Color(0xFF697586), fontSize = 13.sp)
            }
            Text(question.prompt, fontSize = 16.sp, lineHeight = 23.sp, fontWeight = FontWeight.Medium, color = Ink, modifier = Modifier.padding(top = 12.dp, bottom = 10.dp))
            question.choices.forEachIndexed { index, answer ->
                val chosen = choice == index
                val borderColor = if (chosen) Blue else Color(0xFFE1E6EF)
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp).border(1.dp, borderColor, RoundedCornerShape(12.dp))
                    .background(if (chosen) PaleBlue else Color.White, RoundedCornerShape(12.dp))
                    .clickable(enabled = result != true) { onChoice(index) }.padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    RadioButton(selected = chosen, onClick = { if (result != true) onChoice(index) }, enabled = result != true, colors = RadioButtonDefaults.colors(selectedColor = Blue))
                    Text(answer, fontSize = 14.sp, lineHeight = 20.sp, color = Ink)
                }
            }
            if (result == false) {
                Spacer(Modifier.height(9.dp))
                Text("Դեռ ճիշտ չէ։ ${question.explanation} Կարդա բացատրությունը և կրկին փորձիր․ հաջորդ հարցը բացվում է ճիշտ պատասխանից հետո։",
                    Modifier.fillMaxWidth().background(Color(0xFFFFF1F0), RoundedCornerShape(12.dp)).padding(13.dp), color = Red, fontSize = 14.sp, lineHeight = 21.sp)
                Spacer(Modifier.height(10.dp))
                Button(onClick = onRetry, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Կրկին փորձել")
                }
            } else if (result == true) {
                Spacer(Modifier.height(10.dp))
                Text("Ճիշտ է։ ${question.explanation}", Modifier.fillMaxWidth().background(Color(0xFFEAF7EF), RoundedCornerShape(12.dp)).padding(13.dp), color = Green, fontSize = 14.sp, lineHeight = 21.sp)
                Spacer(Modifier.height(10.dp))
                Button(onClick = onContinue, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text(if (questionIndex < lesson.questions.lastIndex) "Հաջորդ հարցը" else "Ավարտել դասը")
                }
                if (questionIndex == lesson.questions.lastIndex) {
                    Text("Դասը կավարտվի, երբ այս հարցին ճիշտ պատասխանես։ Հաջորդ դասը դրանից հետո կբացվի։", fontSize = 12.sp, color = Color(0xFF697586), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                }
            } else {
                Spacer(Modifier.height(10.dp))
                Button(onClick = onCheck, enabled = choice >= 0, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) { Text("Ստուգել պատասխանը") }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

