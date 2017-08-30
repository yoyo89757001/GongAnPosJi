package com.example.xiaojun.gonganposji.beans;

/**
 * Created by chenjun on 2017/5/17.
 */

public class ShiBieBean {


    /**
     * face_info_1 : {"brightness":163.76406533575317,"quality":0.9898704383522272,"rect":{"bottom":103,"left":21,"right":79,"top":46},"std_deviation":21.197982866582855}
     * face_info_2 : {"brightness":109.66754057428214,"quality":0.9284625723958015,"rect":{"bottom":294,"left":236,"right":326,"top":205},"std_deviation":14.867884397758354}
     * same : true
     * score : 90.86259460449219
     */

    private FaceInfo1Bean face_info_1;
    private FaceInfo2Bean face_info_2;
    private boolean same;
    private double score;

    public FaceInfo1Bean getFace_info_1() {
        return face_info_1;
    }

    public void setFace_info_1(FaceInfo1Bean face_info_1) {
        this.face_info_1 = face_info_1;
    }

    public FaceInfo2Bean getFace_info_2() {
        return face_info_2;
    }

    public void setFace_info_2(FaceInfo2Bean face_info_2) {
        this.face_info_2 = face_info_2;
    }

    public boolean isSame() {
        return same;
    }

    public void setSame(boolean same) {
        this.same = same;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public static class FaceInfo1Bean {
        /**
         * brightness : 163.76406533575317
         * quality : 0.9898704383522272
         * rect : {"bottom":103,"left":21,"right":79,"top":46}
         * std_deviation : 21.197982866582855
         */

        private double brightness;
        private double quality;
        private RectBean rect;
        private double std_deviation;

        public double getBrightness() {
            return brightness;
        }

        public void setBrightness(double brightness) {
            this.brightness = brightness;
        }

        public double getQuality() {
            return quality;
        }

        public void setQuality(double quality) {
            this.quality = quality;
        }

        public RectBean getRect() {
            return rect;
        }

        public void setRect(RectBean rect) {
            this.rect = rect;
        }

        public double getStd_deviation() {
            return std_deviation;
        }

        public void setStd_deviation(double std_deviation) {
            this.std_deviation = std_deviation;
        }

        public static class RectBean {
            /**
             * bottom : 103
             * left : 21
             * right : 79
             * top : 46
             */

            private int bottom;
            private int left;
            private int right;
            private int top;

            public int getBottom() {
                return bottom;
            }

            public void setBottom(int bottom) {
                this.bottom = bottom;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public int getRight() {
                return right;
            }

            public void setRight(int right) {
                this.right = right;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }
        }
    }

    public static class FaceInfo2Bean {
        /**
         * brightness : 109.66754057428214
         * quality : 0.9284625723958015
         * rect : {"bottom":294,"left":236,"right":326,"top":205}
         * std_deviation : 14.867884397758354
         */

        private double brightness;
        private double quality;
        private RectBeanX rect;
        private double std_deviation;

        public double getBrightness() {
            return brightness;
        }

        public void setBrightness(double brightness) {
            this.brightness = brightness;
        }

        public double getQuality() {
            return quality;
        }

        public void setQuality(double quality) {
            this.quality = quality;
        }

        public RectBeanX getRect() {
            return rect;
        }

        public void setRect(RectBeanX rect) {
            this.rect = rect;
        }

        public double getStd_deviation() {
            return std_deviation;
        }

        public void setStd_deviation(double std_deviation) {
            this.std_deviation = std_deviation;
        }

        public static class RectBeanX {
            /**
             * bottom : 294
             * left : 236
             * right : 326
             * top : 205
             */

            private int bottom;
            private int left;
            private int right;
            private int top;

            public int getBottom() {
                return bottom;
            }

            public void setBottom(int bottom) {
                this.bottom = bottom;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public int getRight() {
                return right;
            }

            public void setRight(int right) {
                this.right = right;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }
        }
    }
}
