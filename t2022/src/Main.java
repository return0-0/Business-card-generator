import java.util.*;

interface Observer {
    public void update(float temp, float humidity, float cleanness);
}

interface Subject {
    public void registerObserver(Observer o); // 注册对主题感兴趣的观察者
    public void removeObserver(Observer o); // 删除观察者
    public void notifyObservers(); // 当主题发生变化时通知观察者
}

class EnvironmentData implements (1) {
private ArrayList observers;
private float temperature, humidity, cleanness;
public EnvironmentData() { observers = new ArrayList(); }
public void registerObserver(Observer o) { observers.add(o); }
public void removeObserver(Observer o) { /* 代码省略 */ }
public void notifyObservers() {
        for (int i = 0; i < observers.size(); i ++ ) {
        Observer observer = (Observer) observers.get(i);
        (2);
        }
        }
public void measurementsChanged() { (3); }
public void setMeasurements(float temperature, float humidity, float cleanness) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.cleanness = cleanness;
        (4);
        }
        }

class CurrentConditionsDisplay implements (5) {
private float temperature;
private float humidity;
private float cleanness;
private Subject envData;
public CurrentConditionsDisplay(Subject envData) {
        this.envData = envData;
        (6);
        }
public void update(float temperature, float humidity, float cleanness) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.cleanness = cleanness;
        display();
        }
public void display() { /* 代码省略 */ }
        }

class EnvironmentMonitor {
    public static void main(String[] args) {
        EnvironmentData envData = new EnvironmentData();
        CurrentConditionsDisplay currentDisplay = new CnrrentConditionsDisplay(envData);
        envData.setMeasurements(80, 65, 30.4f);
    }
}