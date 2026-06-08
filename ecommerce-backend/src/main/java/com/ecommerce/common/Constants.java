package com.ecommerce.common;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("常量类不能被实例化");
    }

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String CURRENT_USER_ATTR = "currentUserId";

    public static final class OrderStatus {
    private OrderStatus() {}

                public static final int PENDING = 0;
                public static final int PAID = 1;
                public static final int CANCELLED = 2;
    }

    public static final class ProductStatus {
    private ProductStatus() {}

                public static final int ON_SALE = 1;
                public static final int OFF_SALE = 0;
    }

    public static final class UserStatus {
    private UserStatus() {}

                public static final int ACTIVE = 1;
                public static final int DISABLED = 0;
    }
}
