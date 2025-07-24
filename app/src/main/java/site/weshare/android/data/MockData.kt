package site.weshare.android.data

import site.weshare.android.R
import site.weshare.android.model.ExchangeProduct
import site.weshare.android.model.GroupPurchaseProduct

object MockData {
    val groupPurchaseProducts = listOf(
        GroupPurchaseProduct(
            id = "gp_001",
            imageUrl = R.drawable.img_product_samdasoo.toString(),
            name = "제주삼다수 2L",
            totalQuantity = 24, // 이미지: 총 구매 개수: 24개
            quantitySold = 16,  // 계산: 24 (총) - 8 (남은) = 16 (판매)
            remainingDays = 8   // 공동구매 남은 시간(일).
        ),
        GroupPurchaseProduct(
            id = "gp_002",
            imageUrl = R.drawable.img_product_coke_zero.toString(),
            name = "코카콜라 제로 1.5L",
            totalQuantity = 60, // 이미지: 총 구매 개수: 60개
            quantitySold = 45,  // 계산: 60 (총) - 15 (남은) = 45 (판매)
            remainingDays = 15  // 공동구매 남은 시간(일).
        ),
        GroupPurchaseProduct(
            id = "gp_003",
            imageUrl = R.drawable.img_product_revance_tissue.toString(),
            name = "리벤스 물티슈",
            totalQuantity = 80, // 이미지: 총 구매 개수: 80개
            quantitySold = 63,  // 계산: 80 (총) - 17 (남은) = 63 (판매)
            remainingDays = 17  // 공동구매 남은 시간(일).
        ),
        GroupPurchaseProduct(
            id = "gp_004",
            imageUrl = R.drawable.img_product_shinramyun.toString(),
            name = "신라면 멀티팩",
            totalQuantity = 10, // 이미지: 총 구매 개수: 10개
            quantitySold = 9,   // 계산: 10 (총) - 1 (남은) = 9 (판매)
            remainingDays = 10  // 공동구매 남은 시간(일).
        )
    )

    val exchangeProducts = listOf(
        ExchangeProduct(
            id = "ex_001",
            imageUrl = R.drawable.img_exchange_lotte_giants.toString(),
            name = "롯데자이언츠",
            category = "스포츠, 의류",
            exchangeCondition = "희망 카테고리"
        ),
        ExchangeProduct(
            id = "ex_002",
            imageUrl = R.drawable.img_exchange_kerastase.toString(),
            name = "프리미에 디칼슈",
            category = "뷰티/미용, 도서/티",
            exchangeCondition = "희망 카테고리"
        ),
        ExchangeProduct(
            id = "ex_003",
            imageUrl = R.drawable.img_exchange_lenovo_gaming_laptop.toString(),
            name = "레노버 게이밍 노...",
            category = "디지털기기, 게임",
            exchangeCondition = "희망 카테고리"
        ),
        ExchangeProduct(
            id = "ex_004",
            imageUrl = R.drawable.img_exchange_tagheuer.toString(),
            name = "태그호이어 시계",
            category = "의류, 디지털",
            exchangeCondition = "희망 카테고리"
        )
    )
}