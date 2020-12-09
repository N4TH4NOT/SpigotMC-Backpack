package me.n4th4not.backpacks;

public final class Permission {

    public static final A<Void> GAMEMASTER = new A<>("backpacks",0,null);
    public static final A<Integer> LINKED_BACKPACK_LV0 = new A<>("lv0",1,0);
    public static final A<Integer> LINKED_BACKPACK_LV1 = new A<>("lv1",1,1);
    public static final A<Integer> LINKED_BACKPACK_LV2 = new A<>("lv2",1,2);
    public static final A<Integer> LINKED_BACKPACK_LV3 = new A<>("lv3",1,3);
    public static final A<Integer> LINKED_BACKPACK_LV4 = new A<>("lv4",1,4);
    public static final A<Integer> LINKED_BACKPACK_LV5 = new A<>("lv5",1,5);
    public static final A<Integer> LINKED_BACKPACK_LV6 = new A<>("lv6",1,6);
    public static final A<Void> NO_LINKED_BACKPACK_BUTTON = new A<>("no_button",1,null);
    public static final A<Void> LINKED_BACKPACK_SEE_OTHERS = new A<>("others",1,null);
    public static final A<Void> LINKED_BACKPACK_EDIT_OTHERS = new A<>("others.edit",1,null);
    public static final A<Void> LINKED_BACKPACK_IGNORE_DEATH = new A<>("safe",1,null);
    public static final A<Void> LINKED_BACKPACK_LOCKDOWN = new A<>("lockdown",1,null);

    public static final class A<T> {

        public static final String PREFIX = "eliaze";
        public static final String LINKED_BACKPACK = "linked_backpack";

        public final String perm;
        public final T value;

        A(String var0, int var1, T var3) {
            this.perm = Utilities.Text.a(".",PREFIX,a(var1),var0);
            this.value = var3;
        }

        private static String a(int var0) {
            switch (var0) {
                case 0: return "";
                case 1: return LINKED_BACKPACK;
                default:
                    throw new IllegalStateException("Not implemented yet.");
            }
        }
    }
}