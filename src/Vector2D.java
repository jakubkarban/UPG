public class Vector2D<T> {
    
        public T x;
        public T y;
    
        public Vector2D(T x, T y){
            this.x = x;
            this.y = y;
        }
    
        public T getX() {
            return x;
        }
    
        public T getY() {
            return y;
        }
    
        public void setX(T x) {
            this.x = x;
        }
    
        public void setY(T y) {
            this.y = y;
        }
    
        public void setVector(Vector2D<T> v){
            this.x = v.x;
            this.y = v.y;
        }
    
        public Vector2D<T> getVector(){
            return this;
        }
    
        public Vector2D<Double> decreasing(Vector2D<Double> v){
            return new Vector2D((double)x-v.x,(double)y-v.y);
        }
    
        public Vector2D<Double> increasing(Vector2D<Double> v){
            return new Vector2D((double)x+v.x,(double)y+v.y);
        }
    
        public Vector2D<Double> partition(Vector2D<Double> v){
            if(v.x==0&&v.y==0){
                return new Vector2D<>(0.0,0.0);
            }
            if(v.x==0){
                return new Vector2D(0.0,(double)y/v.y);
            }
            if(v.y==0){
                return new Vector2D((double)x/v.x,0.0);
            }
            return new Vector2D((double)x/v.x,(double)y/v.y);
        }
    
        public Vector2D<Double> partition(double v){
            if(v==0){
                return new Vector2D(0.0,0.0);
            }
            return new Vector2D((double)x/v,(double)y/v);
        }
    
        public Vector2D<Double> expand(Vector2D<Double> v){
            return new Vector2D((double)x*v.x,(double)y*v.y);
        }
    
        public Vector2D<Double> expand(double v){
            return new Vector2D((double)x*v,(double)y*v);
        }
    
        public Vector2D<Double> abs(){
            return new Vector2D(Math.abs((double)x),Math.abs((double)y));
        }
    
        public Vector2D<Double> pow(int pow){
            return new Vector2D(pow((double)x,pow),pow((double)y,pow));
        }
    
        public Vector2D<Double> pow(double pow){
            return new Vector2D(pow((double)x,pow),pow((double)y,pow));
        }
    
        public Vector2D<Double> sqrt(){
            return new Vector2D(Math.sqrt((double)x),Math.sqrt((double)x));
        }
    
        public double lenght(){
            return Math.sqrt(pow((double)x,2)+pow((double)y,2));
        }
    
        public static double pow(double x, int pow){
            double result = 1;
            for (int i = 0; i < pow; i++){
                result *= x;
            }
            return result;
        }
    
        public static double pow(double x, double pow){
            return Math.pow(x,pow);
        }
}
    
