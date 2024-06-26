package com.example.magicrecipes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);
    }
    // Кнопка выхода из аккаунта
    public void ExtAccBttn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы хотите выйти из аккаунта?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Действие при подтверждении выхода из аккаунта
                logout(); // Метод для выхода из аккаунта
            }
        });
        builder.setNegativeButton("Нет", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void FavouriteButton(View view) {
        Intent intent = new Intent(LunchActivity.this, FavoriteActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void CatalogButton(View view) {
        if (isInternetAvailable()) {
            Intent intent = new Intent(LunchActivity.this, CatalogActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            showNoInternetMessage();
        }
    }

    public void BackToMain(View view) {
        if (isInternetAvailable()) {
            Intent intent = new Intent(LunchActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            showNoInternetMessage();
        }
    }

    // Метод для проверки подключения к интернету
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    // Метод для отображения сообщения об отсутствии интернета
    private void showNoInternetMessage() {
        Toast.makeText(this, "Отсутствует подключение к интернету", Toast.LENGTH_SHORT).show();
    }

    // Метод для выхода из аккаунта
    private void logout() {
        // выход из Firebase
        FirebaseAuth.getInstance().signOut();
        // Переход на экран загрузки
        Intent intent = new Intent(LunchActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    private void addToFavorites(Recipe recipe) {
        // Сохраняем рецепт в SharedPreferences в виде JSON
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(recipe.getName(), recipe.toJson());
        editor.apply();

        // Подтверждение сохранения
        Toast.makeText(this, "Рецепт добавлен в избранное.", Toast.LENGTH_SHORT).show();
    }


    private void openRecipe(Recipe recipe) {
        // Открываем рецепт
        Intent intent = new Intent(LunchActivity.this, ProfileRecipe.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    private void setRecipeClickListeners(View view, final Recipe recipe) {
        // Обработчик длинного нажатия
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Создаем диалоговое окно для подтверждения удаления
                AlertDialog.Builder builder = new AlertDialog.Builder(LunchActivity.this);
                builder.setTitle("");
                builder.setMessage("Добавить в избранное?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Добавляем рецепт в FavoriteActivity
                        addToFavorites(recipe);
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ничего не делаем, закрываем диалоговое окно
                        dialog.dismiss();
                    }
                });
                // Показываем диалоговое окно
                builder.create().show();

                return true;
            }
        });

        // Обработчик обычного нажатия
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываем рецепт
                openRecipe(recipe);
            }
        });
    }

    public void firstLunchOpen(View view) {
        final Recipe recipe = new Recipe("Окрошка на минералке с копченой куриной грудкой", "Для одной порции:\n\nМинеральная вода: 200 мл\n\nКартошка 240 г (2 шт.)\n\nКуриное яйцо 60 г (1 шт.)\n\nЗелёный лук 16.5 г (0,33 пучка)\n\nОгурец 100 г (1 шт.)\n\nРедис 60 г (3 шт.)\n\nКопчёная курица 100 г\n\nПетрушка 6.6 г (0.33 пучка)\n\nУкроп 4.95 г (0.33 пучка)\n\nСметана 15% 25 г (1 ст. л.)\n\nМайонез 25 г (1 ст. л.)\n\nСоль 2.5 г (0.25 ч. л.)\n\nЧёрный молотый перец 1.25 г (0.25 ч. л.)\n\nВинный белый уксус 18 г (1 ст. л.)\n\nСахар 5 г (0.5 ч. л.)\n\n\n___\n\n\nПомойте картофель, куриное яйцо, зелень, огурец, редис.\n\nОтварите картофель в течение 30 минут. Остудите и очистите от кожуры. Нарежьте небольшими кубиками. Запеките картошку в духовке, завернув в фольгу.\n\nОтварите куриное яйцо вкрутую в течение 8-10 минут. Остудите в холодной воде и очистите от скорлупы.\n\nНарежьте маленькими кубиками яйцо, грудку, редис и огурец. Натрите огурец и редис или нарежьте кружочками.\n\nМелко покрошите зеленый лук, укроп и петрушку.\n\nСмешайте в миске все нарезанные продукты со сметаной и майонезом. Добавьте соль, черный молотый перец и сахар.\n\nЗалейте окрошку минеральной водой до нужной густоты. Добавьте винный уксус. Перемешайте и уберите в холодильник на 1 час.", R.drawable.firstlunch);

        // Обработчики  нажатий
        setRecipeClickListeners(view, recipe);

    }

    public void secondLunchOpen(View view) {
        final Recipe recipe = new Recipe("Лагман с редькой", "Для одной порции:\n\nГовядина 166.67 г\n\nТоматная паста 10 г (0.33 ст. л.)\n\nЗеленая редька 100 г\n\nМорковь 30 г\n\nРепчатый лук 26.67 г\n\nКартошка 40 г\n\nБолгарский перец 25 г\n\nЧеснок 3.33 г\n\nКопченая паприка 2.33 г\n\nСахар 1.67 г (0.17 ч. л.)\n\nСоль 3.33 г (0.33 ч. л.)\n\nВода 466.67 г (2.33 стакана)\n\nЛагманная лапша 83.33 г\n\nПодсолнечное масло рафинированное 5.67 г (0.33 ст. л.)\n\n\n___\n\n\nПромойте говядину, промокните ее бумажным полотенцем. Вымойте и почистите редьку, картофель, морковь, лук, чеснок и болгарский перец. Достаньте глубокую сковороду, кастрюлю и дуршлаг.\n\nНарежьте говядину кубиками со стороной 1,5-2 см. Точно так же измельчите картофель. Чуть меньшими кубиками порубите морковь, редьку и болгарский перец. Мелко покрошите лук и чеснок.\n\nНалейте в сковороду растительное масло, разогрейте его на сильном огне. Обжарьте кубики говядины со всех сторон до плотной коричневой корочки в течение 10 минут.\n\nВсыпьте к мясу ⅔ ч.л. соли и паприку. Положите в сковороду лук, морковь, картофель, редьку и болгарский перец. Обжарьте все вместе, помешивая, около 4-5 минут.\n\nПоложите к овощам с мясом чеснок и томатную пасту, всыпьте сахар. Все хорошо перемешайте. Залейте ингредиенты 2 стаканами воды. Уменьшите огонь до слабого, накройте сковороду крышкой и потушите овощи с говядиной около 15-20 минут.\n\nНалейте в кастрюлю 1 л воды, доведите ее до кипения на сильном огне. Убавьте огонь до среднего, всыпьте в воду ⅓ ч.л. соли. Положите в воду лапшу для лагмана и отварите ее в течение 5 минут. Откиньте лапшу на дуршлаг и промойте холодной водой.\n\nРазлейте бульон с мясом и овощами по тарелкам, сверху выложите лапшу.", R.drawable.secondlunch);

        // Обработчики  нажатий
        setRecipeClickListeners(view, recipe);
    }

    public void thirdLunchOpen(View view) {
        final Recipe recipe = new Recipe("Куриный суп с щавелем", "Для одной порции:\n\nВода 1 л (1000 г)\n\nКуриное филе 1 шт. (200 г)\n\nКартошка 2 шт. (240 г)\n\nРепчатый лук 1 шт. (80 г)\n\nМорковь 1 шт. (90 г)\n\nЩавель 200 г\n\nЛавровый лист 1 шт. (1 г)\n\nСоль 1 ч. л. (10 г)\n\nЧерный перец горошком 0.5 ч. л. (1 г)\n\nРастительное масло 1 ст. л. (17 г)\n\n\n___\n\n\nРазморозьте, помойте и обсушите куриное филе. Помойте и почистите картошку, лук и морковь. Помойте щавель.\n\nПоложите куриное филе в кастрюлю, залейте водой. Поставьте на сильный огонь. Доведите до кипения, снимите пену. Посолите, добавьте перец горошком и лавровый лист. Варите 30 минут. Добавьте в воду целиком луковицу, морковку и корень сельдерея, чтобы бульон получился вкуснее.\n\nНарежьте картошку брусочками. Мелко покрошите репчатый лук. Натрите морковь.\n\nДостаньте курицу из кастрюли, мелко нарежьте. Процедите бульон и верните его на огонь.\n\nКогда бульон закипит, положите в него картошку. Варите 15 минут.\n\nРазогрейте в сковороде растительное масло. Обжарьте лук с морковью 5 минут на среднем огне, регулярно перемешивая.\n\nПоложите в суп обжарку из овощей. Перемешайте. Варите 5 минут. Добавьте по вкусу различные специи и зелень. Попробуйте суп на соль и досолите, если нужно.\n\nНарежьте щавель помельче, добавьте в суп. Хорошо все перемешайте и отключите нагрев. Дайте готовому супу настояться под крышкой 5-10 минут.\n\nМелко покрошите вареное яйцо. Добавьте его в порцию супа.", R.drawable.thirdlunch);
        // Обработчики  нажатий
        setRecipeClickListeners(view, recipe);
    }

    public void fourthLunchOpen(View view) {
        final Recipe recipe = new Recipe("Суп «Тертюха»", "Для одной порции:\n\nГовядина на кости 175 г\n\nВода 500 г\n\nКартошка 60 г\n\nРепчатый лук 20 г\n\nСливочное масло 2.5 г\n\nПолукопченая колбаса 25 г\n\nСливки 10% жирности 35 г\n\nСоль 2.5 г\n\n\n___\n\n\nРазморозьте, помойте и обсушите кусок говядины. Помойте и почистите картофель и репчатый лук.\n\nПоложите говядину в небольшую кастрюлю. Залейте водой и поставьте на сильный огонь. После закипания снимите пену, убавьте огонь до минимального, посолите. Варите 1 час под крышкой. Процедите готовый бульон. Мясо, на котором варился бульон, в этом рецепте больше не понадобится. Можно использовать его для приготовления других блюд, например, нарезать его для салата.\n\nМелко покрошите репчатый лук. Растопите в сковороде сливочное масло. Жарьте лук в течение 5 минут на среднем огне, регулярно перемешивая.\n\nНатрите картофель на крупной терке. Доведите бульон до кипения и положите в него картофель. Перемешайте. Варите 15 минут на среднем огне.\n\nДобавьте в суп лук и влейте сливки. Перемешайте. Дождитесь повторного закипания и отключите нагрев.\n\nНарежьте колбасу тонкой соломкой. Обжарьте ее на той же сковороде, где жарился лук, в течение 3 минут. Вместо колбасы подойдут копченые сосиски, грудинка, шкварки или другие мясные полуфабрикаты.\n\nНалейте в миску порцию супа, положите в него жареную колбасу.", R.drawable.fourthlunch);
        // Обработчики  нажатий
        setRecipeClickListeners(view, recipe);
    }

    public void fifthLunchOpen(View view) {
        final Recipe recipe = new Recipe("Венгерский мясной суп", "Для шести порций:\n\nБаранина 166.67 г\n\nКурдючный жир 8.33 г (0.17 ст. л.)\n\nКартошка 80 г\n\nРепчатый лук 53.33 г\n\nЧеснок 3.33 г\n\nСметана 20% жирности 33.33 г\n\nПшеничная мука хлебопекарная 8.33 г (0.33 ст. л.)\n\nЛимон 7.5 г (0.25 шт.)\n\nВода 1000 г\n\nСушеный укроп 0.5 г (0.33 ч. л.)\n\nЛавровый лист 1 г\n\nСушеная петрушка 0.5 г (0.33 ч. л.)\n\nСушеный тархун / эстрагон 0.5 г (0.33 ч. л.)\n\nМолотый тмин 3 г (0.67 ч. л.)\n\nСладкая паприка 18 г (3 ч. л.)\n\nЧерный перец молотый 1.25 г\n\nСоль 10 г (1 ч. л.)\n\nСтручковая фасоль 175 г\n\n\n___\n\n\nРазморозьте, помойте и обсушите баранину. Ополосните и почистите картошку, лук и чеснок. Помойте лимон. Подготовьте кастрюлю с толстым дном.\n\nИзмельчите курдючный жир на маленькие кусочки. Нарежьте мякоть баранины небольшими кубиками, картофель — брусочками. Мелко покрошите репчатый лук. Пропустите чеснок через пресс. Чтобы картофель не потемнел и не заветрился, залейте его холодной водой.\n\nНагрейте кастрюлю на среднем огне. Положите в нее курдючный жир. Жарьте 5 минут, периодически помешивая. Уберите образовавшиеся шкварки из кастрюли. Они больше не понадобятся. Этот суп можно варить в мультиварке.\n\nПоложите в кастрюлю баранину. Жарьте на сильном огне 5 минут, часто помешивая.\n\nДобавьте лук, лавровый лист, тмин, перец, соль, эстрагон, петрушку, укроп, паприку и чеснок. Перемешайте. Готовьте еще 3 минуты на среднем огне.\n\nВлейте 0,5 л воды в кастрюлю. Доведите до кипения. Уменьшите нагрев до минимального, накройте кастрюлю крышкой и варите 1 час.\n\nВлейте оставшуюся воду. Добавьте картошку, зеленую фасоль. Варите еще 15 минут после повторного закипания.\n\nСмешайте в миске сметану, сок от ¼ лимона с мукой. Добавьте 0,5 стакана бульона из кастрюли. Тщательно перемешайте.\n\nВлейте сметанную массу в суп. Хорошо перемешайте.", R.drawable.fivethlunch);
        // Обработчики  нажатий
        setRecipeClickListeners(view, recipe);
    }

    public void sixthLunchOpen(View view) {
        final Recipe recipe = new Recipe("Куриные котлеты с геркулесом", "Для шести порций:\n\nЦельная овсянка (геркулес) 140 г\n\nКуриное филе 300 г\n\nПолутвердый сыр 70 г\n\nМолоко 2,5% жирности 100 мл = 100 г\n\nПшеничная мука хлебопекарная 2 ст. л. = 50 г\n\nКуриное яйцо 1 шт. = 60 г\n\nСтебель сельдерея 0.15 г\n\nСоль 3.3 г (0.33 ч. л.)\n\nЧерный перец молотый 1 г (1 щепотка)\n\nПодсолнечное масло рафинированное 17 г (1 ст. л.)\n\n\n___\n\n\nПомойте и промокните салфеткой яйцо и стебель сельдерея. Подготовьте емкость, подходящую для микроволновки, мясорубку, терку, миску, плоскую тарелку и сковороду.\n\nНалейте молоко в емкость, подходящую для микроволновой печи. Подогрейте молоко 30-40 секунд до теплого состояния. Всыпьте в теплое молоко геркулес, перемешайте и оставьте его на 1 час.\n\nНарежьте куриное филе небольшими кусочками, пропустите их через мясорубку в миску. В эту же миску мелко натрите сыр и корень сельдерея.\n\nРазбейте в миску с измельченными продуктами яйцо, всыпьте соль и перец. Хорошо все размешайте.\n\nВыложите набухший в молоке геркулес в миску к фаршу, еще раз все перемешайте. Вымесите фарш до гладкой однородной консистенции. Если фарш получится жидким, всыпьте в него 0,5-1 ст.л. муки.\n\nВысыпьте муку на плоскую тарелку. Слепите из фарша круглые котлеты размером около 5 см в диаметре. Обваляйте котлеты в муке со всех сторон.\n\nНалейте в сковороду растительное масло, разогрейте его на сильном огне. Опустите в масло подготовленные котлеты, жарьте их 4-5 минут с одной стороны до плотной румяной корочки. Переверните котлеты и жарьте их с другой стороны еще 4-5 минут до появления корочки.", R.drawable.sixthlunch);
        // Обработчики  нажатий
        setRecipeClickListeners(view, recipe);
    }

    public void seventhLunchOpen(View view) {
        final Recipe recipe = new Recipe("Суп с ячневой крупой", "Для четырёх порций:\n\nВода 466.67 г (2.33 стакана)\n\nКуриные крылышки 100 г (1 шт.)\n\nМорковь 90 г (1 шт.)\n\nРепчатый лук 80 г (1 шт.)\n\nКартошка 120 г (3 шт.)\n\nЯчневая крупа 20 г (2 ст. л.)\n\nСливочное масло 5 г (1 ч. л.)\n\nСоль по вкусу\n\n\n___\n\n\nПомойте куриные крылышки и обсушите их бумажными полотенцами. Вымойте и почистите овощи. Промойте ячневую крупу, пока вода не станет прозрачной.\n\nРазрежьте куриные крылья на несколько частей. Выложите их в кастрюлю и залейте холодной водой. Поставьте кастрюлю на максимальный огонь. Дайте воде закипеть и аккуратно снимите образовавшуюся пенку. Убавьте огонь до среднего. Варите крылышки под крышкой около 30 минут. Удобнее всего пенку можно снять небольшим ситечком с длинной ручкой.\n\nЧерез 30 минут добавьте к мясу ячневую крупу. Посолите по вкусу и перемешайте. Варите на среднем огне еще 20 минут. Не кладите крупы больше, чем указано в рецепте. Иначе суп получится излишне густым.\n\nНарежьте картофель небольшими кубиками. Добавьте его в суп. Перемешайте ингредиенты и варите до полуготовности картошки примерно 7-10 минут.\n\nНатрите морковь на средней терке. Нашинкуйте репчатый лук тонкими перьями.\n\nРастопите сливочное масло в сковороде с толстым дном. Отправьте в сковороду лук и морковь. Жарьте их, помешивая, в течение 5 минут.\n\nПереложите овощную зажарку в кастрюлю с остальными ингредиентами. Все перемешайте и варите суп еще пару минут. Попробуйте блюдо на вкус. При необходимости посолите. Затем выключите огонь и дайте настояться супу 5 минут под крышкой.", R.drawable.seventhlunch);
        // Обработчики  нажатий
        setRecipeClickListeners(view, recipe);
    }

    public void eighthLunchOpen(View view) {
        final Recipe recipe = new Recipe("Суп-пюре из зеленой чечевицы", "Для четырёх порций:\n\nЗеленая чечевица 160 г\n\nВода 400 мл (400 г)\n\nМидии 14 шт. (308 г)\n\nРепчатый лук 1 шт. (80 г)\n\nРастительное масло 2 ст. л. (34 г)\n\nСтебель сельдерея 45 г\n\nМолотая куркума 1 щепотка (1 г)\n\nМайоран 1 щепотка (1 г)\n\nСушеный орегано (душица) 1 щепотка (1 г)\n\nСмесь перцев 1 щепотка (1 г)\n\nБелое сухое вино 350 мл (350 г)\n\nСоль 1 ч. л. (10 г)\n\nСахар 1 щепотка (1 г)\n\n\n___\n\n\nПромойте чечевицу под проточной водой. Разморозьте мидии, удалите из них внутренности и желудок, помойте. Почистите репчатый лук. Ополосните сельдерей. Подготовьте погружной блендер.\n\nНасыпьте чечевицу в небольшую кастрюлю. Залейте водой и поставьте на средний огонь. Варите 40 минут после закипания. Посолите в самом конце приготовления.\n\nПока варится чечевица, нарежьте репчатый лук тонкими полукольцами. Чтобы не слезились глаза во время нарезки лука, смочите луковицу и лезвие ножа в холодной воде.\n\nРазогрейте в сковороде растительное масло. Обжарьте лук в течение 5 минут на среднем огне, регулярно перемешивая.\n\nМелко покрошите стебель сельдерея.\n\nДобавьте сельдерей к луку. Жарьте еще 5 минут.\n\nДобавьте в сковороду к овощам куркуму, майоран, орегано, смесь перцев. Хорошо перемешайте.\n\nВлейте к овощам вино. Доведите до кипения. Варите 3-4 минуты. Вино нужно обязательно проварить, чтобы выпарился алкоголь.\n\nПосолите, добавьте сахар. Перемешайте соус.\n\nПоложите в соус мидии. Доведите до кипения и сразу отключите нагрев.\n\nСлейте с чечевицы жидкость в миску. Переложите чечевицу к остальным ингредиентам и пробейте погружным блендером до получения однородной консистенции.", R.drawable.eigthlunch);
        // Обработчики  нажатий
        setRecipeClickListeners(view, recipe);
    }

    public void ninthLunchOpen(View view) {
        final Recipe recipe = new Recipe("Гречка с говяжьей тушенкой и луком", "Для четырёх порций:\n\nГречка 247.5 г\n\nГовяжья тушенка 200 г\n\nРепчатый лук 80 г\n\nМорковь 40 г\n\nТоматная паста 30 г\n\nВода 400 мл\n\nСоль 10 г (1 ч. л.)\n\nСливочное масло 120 г\n\n\n___\n\n\nПромойте под проточной водой гречку. Сполосните и почистите репчатый лук и морковь.\n\nМелко покрошите репчатый лук. Натрите морковь. Измельчите овощи при помощи кухонного комбайна.\n\nРастопите в глубокой сковороде сливочное масло. Обжарьте лук на среднем огне в течение 3 минут, регулярно перемешивая.\n\nДобавьте на сковороду морковь, жарьте все вместе еще 5 минут.\n\nВыложите в сковороду тушенку и томатную пасту. Хорошенько перемешайте. Готовьте 10-15 минут, периодически помешивая. Добавьте больше специй: острый перец чили, чеснок, прованские травы и другие.\n\nВсыпьте в сковороду гречку, влейте стакан кипятка. Посолите, перемешайте. Накройте крышкой. Томите кашу 15 минут на слабом огне.\n\nДополните подачу ломтиками маринованного огурца, зелёным луком и укропом.", R.drawable.ninthlunch);
        // Обработчики  нажатий
        setRecipeClickListeners(view, recipe);
    }

    public void tenthLunchOpen(View view) {
        final Recipe recipe = new Recipe("Запеканка с фаршем в микроволновке", "Для двух порций:\n\n" +
"Картошка 120 г\n\n" +
"Говяжий фарш 100 г\n\n" +
"Репчатый лук 20 г\n\n" +
"Куриное яйцо 60 г\n\n" +
"Твердый сыр 25 г\n\n" +
"Молоко 2,5% жирности 50 г\n\n" +
"Растительное масло 17 г\n\n" +
"Соль 5 г\n\n" +
"Черный перец молотый 1.25 г\n\n" +
"Сливочное масло 20 г\n\n\n___\n\n\nПомойте и почистите картошку и репчатый лук. Разморозьте фарш. Помойте куриное яйцо. Подготовьте посуду, в которой можно готовить в микроволновке.\n\nНарежьте крупно картошку, положите в кастрюлю и залейте водой. Доведите до кипения на сильном огне, уменьшите нагрев и варите 20 минут под крышкой.\n\nСлейте с картошки воду и разомните ее в пюре при помощи толкушки. Остудите пюре.\n\nРазбейте в пюре яйцо, влейте молоко. Посолите, поперчите и хорошенько перемешайте. Добавьте и другие специи: красный перец, паприку, мускатный орех, прованские травы.\n\nМелко покрошите репчатый лук. Смешайте лук с фаршем. Положите в тарелку, закройте крышкой и поставьте в микроволновку на 6 минут при максимальной мощности.\n\nСмажьте форму для микроволновки сливочным маслом. Выложите половину картофельного пюре. Сверху ровным слоем положите фарш. Посолите и поперчите его.\n\nНа фарш выложите оставшееся пюре. Натрите сыр и посыпьте запеканку. Накройте ее крышкой и отправьте в микроволновку на 20 минут и 80% мощности. Если у вас есть режим Гриль на микроволновке, включите его на последние 5 минут.\n\nПодайте запеканку с помидорами черри.", R.drawable.tenthlunch);
        // Обработчики  нажатий
        setRecipeClickListeners(view, recipe);
    }
}