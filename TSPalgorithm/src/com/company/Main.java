package com.company;


import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Main {


    public static void main(String[] args) throws IOException {

        ArrayList<Point> pointList = new ArrayList<Point>();
        ArrayList<Path> pathList = new ArrayList<Path>();
        ArrayList<Path> firstPathList = new ArrayList<Path>();
        ArrayList<Path> mutatedPathList = new ArrayList<Path>();
        ArrayList<Path> newPathList = new ArrayList<Path>();
        ArrayList<Point> greedyList = new ArrayList<Point>();
        int initialPopulation = 100; // to i to nizej musi byc tak samo bo wyjebie petle
        int population = 100;
        int percentOfMutation = 20;




        pointList = loadData("F:/TSPalgorithm/instancje/instancja.txt");   //instancja bier127 berlin52
        //checkVisited(pointList);

        System.out.println(greedy(pointList));

        pathList = startedPopulation(pointList, 1000000, initialPopulation); //10000 200
        System.out.println("Pokolenie 1");
        firstPathList = crossover(pathList, 20000 , population); //200 100
        newPathList=firstPathList;
        firstPathList = mutation(newPathList, population, percentOfMutation);


        for (int i=0; i<10000; i++){
            System.out.println("Pokolenie " + (i+2));
            newPathList=crossover(firstPathList,2000,population);
            firstPathList=mutation(newPathList,population, percentOfMutation);

        }


        for (int i=0; i<firstPathList.size();i++){
            System.out.println(i);
            System.out.println(firstPathList.get(i));
        }


    }
    //wczytanie danych z pliku
    public static ArrayList<Point> loadData(String path) throws FileNotFoundException {

        File file = new File(path);
        Scanner scanner = new Scanner(file);

        ArrayList<Point> list = new ArrayList<Point>();

        //zapisanie danych arraylist
        scanner.nextLine();
        while (scanner.hasNextLine()){
            String[] pointinfo = scanner.nextLine().trim().replaceAll("\\s{2,}"," ").split(" ");
            int number = Integer.parseInt(pointinfo[0]);
            int xArray = Integer.parseInt(pointinfo[1]);
            int yArray = Integer.parseInt(pointinfo[2]);
            list.add(new Point(number, xArray, yArray));
        }
        return list;
    }

    //generacja listy randomowych punktow z zakresu, dla podanej liczby punktow
    public static ArrayList<Point> randomList(int number, int range){
        ArrayList<Point> list = new ArrayList<Point>();
        Random randomGenerator = new Random();
        for (int i=0; i<number; i++){
            int numberR = i+1;
            int xArrayR = randomGenerator.nextInt(range);
            int yArrayR = randomGenerator.nextInt(range);
            list.add(new Point(numberR, xArrayR, yArrayR));
        }
        return list;
    }


    //ALGORYTM ZACHłANNY
    public static double greedy(ArrayList<Point> loadList){
        ArrayList<GreedyPoint> visitedList = new ArrayList<GreedyPoint>();
        System.out.println("ALGORYTM ZACHLANNY");

        for (int i=0; i<loadList.size(); i++){
            visitedList.add(new GreedyPoint(loadList.get(i).getNumber(), loadList.get(i).getX(), loadList.get(i).getY(), false));
        }

        double distanceTraveled = 0;
        //ustawienie pierwszego punktu jako odwiedzonego
        visitedList.get(0).setVisited(true);

        int currentPoint=0;
        int currentPointJ = 0;
        for (int i=0; i<loadList.size()-1;i++){
            double min = 2147483647;
            for (int j=1; j<loadList.size();j++){
                double section= distance(loadList.get(currentPoint).getX(),loadList.get(currentPoint).getY(),loadList.get(j).getX(),loadList.get(j).getY());
                if (section < min && !visitedList.get(j).isVisited()){
                    min = section;
                    currentPointJ = loadList.get(j).getNumber()-1;
                }
            }
            distanceTraveled+=min;
            visitedList.get(currentPointJ).setVisited(true);
            currentPoint=currentPointJ;
            //System.out.println("Odwiedzono punkt: " + (currentPointJ+1) + " i przebyto łącznie " + distanceTraveled);

        }
        //dodanie odcinka łączącego ostatni punkt z punktem startowym
        //System.out.println("Teraz osiągnelismy ostatni punkt na trasie, czyli: " + (currentPointJ+1) + " i wracamy z niego do punktu 1");
        distanceTraveled+= distance(loadList.get(currentPoint).getX(),loadList.get(currentPoint).getY(),loadList.get(0).getX(),loadList.get(0).getY());
        //System.out.println("Odległość po odwiedzeniu wszytskich punktów i wróceniu do punktu startowego wynosi " + distanceTraveled);
        return distanceTraveled;
    }

    //ALGORYTM GENETYCZNY
    //UTWORZENIE PIERWSZEJ POPULACJI I WYBRANIE Z NIEJ NAJKRÓTSZYCH SCIEŻEK
    public static ArrayList<Path> startedPopulation(ArrayList<Point> pointList, int firstPopulation, int population){
        Point first = new Point();
        ArrayList<Path> pathList = new ArrayList<>();
        first = pointList.get(0);
        double min = 999999999;
        double distance;
        ArrayList<Point> route = new ArrayList<Point>();

        for (int j = 0; j<firstPopulation;j++){
            //usuniecie pierwszego punktu, potem wymieszanie i nastepnie ponowne jego dodanie
            pointList.remove(0);
            Collections.shuffle(pointList);
            pointList.add(0, first);
            distance = calculatePath(pointList);
            pathList.add(new Path(distance, new ArrayList<>(pointList)));
            if (distance < min){
                min =distance;
            }
        }

        System.out.println("Minimalna wartosc w pierwszej populacji " + min);

        Collections.sort(pathList, Comparator.comparing(Path::getDistance));

        //wybieranie dalej x najlepszych sciezek
        for (int i=pathList.size(); i>population; i--){
            pathList.remove(pathList.size()-1);
        }


        return pathList;
    }

    //KRZYŻOWANIE ze soba losowych sciezek w populacji i tworzenie w ten sposob nowych sciezek,
    //nastepnie znowu segregaacja i wybranie najlepszych sciezek
    public static ArrayList<Path> crossover(ArrayList<Path> list, int newPopulation, int population){

        int halfPathSize = (Integer) list.get(0).getPath().size() / 2; //połowa punktów w scieżce

        int range= list.size()-1;                               //zakres losowanych ścieżek
        List<Point> firstParentHalf = new ArrayList<Point>();   //polowa pierwszej wylosowanej listy
        List<Point> secondParent = new ArrayList<Point>();;      //druga wylosowana lista
        ArrayList<Point> finalList = new ArrayList<Point>();     //nowa lista

        double min = 999999999;

        //ścieżeki do mieszania
        int first, second;
        Random randomFirst = new Random();
        Random randomSecond = new Random();


        //KRZYZOWANIE
        //tworzenie nowego pokolenia, wielkosc i mozna sobie zmieniac, tutaj jest tyle nowych sciezek co wielkosc poulacji startowej
        for (int j=0; j<newPopulation;j++){
            first = randomFirst.nextInt(range);
            firstParentHalf = list.get(first).getPath().subList(0, halfPathSize); //pierwsza połowa pierwszego wylosowanego rodzica
            finalList.addAll(firstParentHalf);                                    //dodanie pierwszej połowy do nowej listy

            //dodajemy do pierwszej polowy druga liste
            second =randomSecond.nextInt(range);                                  //losowanie drugiej ścieżki
            secondParent = list.get(second).getPath();                            //druga ścieżka
            //dodawanie elementów drugiej listy, które nie wystepowały w pierwszej
            for(int x=0; x<secondParent.size(); x++){
                if(!finalList.contains(secondParent.get(x))){
                    finalList.add(secondParent.get(x));
                }

            }

            //dodanie do listy nowo utworzonych ścieżek, pominięcie ścieżek co juz występują w liście
            list.add(new Path(calculatePath(finalList), new ArrayList<Point>(finalList)));

            finalList.clear();

        }

        //usunięcie powtarzających sie ścieżek z rozwiązania
        Set<Path> noDupList = new HashSet<>(list);
        list.clear();
        list.addAll(noDupList);
        //posortowanie listy według dystansu
        list.sort(Comparator.comparing(Path::getDistance));
        //wypisanie najlepszego wyniku z nowego pokolenia
        System.out.println("Wartość min w pokoleniu po krzyżowaniu:: " + list.get(0).getDistance());

        //usuunięcie najbardziej niekorzystnych ścieżek
        for (int k=list.size(); k>population; k--){
            list.remove(list.size()-1);
        }

        return list;

    }
    //3 MUTACJA wybranie % losowych ścieżek z populaji i zamiana dwóch punktów
    public static ArrayList<Path> mutation (ArrayList<Path> list, int population, int percentOfMutation){

        Random randomFirstNumber = new Random();                                //losowy element z wybranej ściezki
        Random randomSecondNumber = new Random();                               //drugi losowy element z wybranej ścieżki
        int firstNumber, secondNumber, selectedPathNumber;                      //numery wybranych punktów, ścieżek
        Random randomPath = new Random();   //losowa ścieżka z puli
        Point firstPoint = new Point();     //pierwszy wylosowany punkt
        Point secondPoint = new Point();    //drugi wylosowany punkt
        int range = list.size()-1;          //liczba ścieżek
        int numberOfMutations = (Integer) list.size()/(100/percentOfMutation);  //liczna mutacji
        Point startedPoint = new Point();   //pierwszy punkt ze sciezki, ktorego nie bedziemy nigdy zamieniac
        ArrayList<Point> selectedPath = new ArrayList<Point>();
        double distance;
        for (int i=0; i<numberOfMutations; i++){


            selectedPathNumber = randomPath.nextInt(range);                             //wylosowana nr ścieżki do mutacji
            selectedPath = new ArrayList<>(list.get(selectedPathNumber).getPath());     //wylosowana ściezka do mutacji
            startedPoint = selectedPath.get(0);                                         //pierwszy punkt z wylosowanej ścieżki
            selectedPath.remove(0);                                               //usuniecie pierwszego punktu z wylosowanej ścieżki
            firstNumber = randomFirstNumber.nextInt(selectedPath.size());               //wyloswanie nr pierwszego punktu do mutacji
            secondNumber =randomSecondNumber.nextInt(selectedPath.size());              //wylosowanie nr drugiego punktu do mutacji
            firstPoint = selectedPath.get(firstNumber);                                 //pierwszy punkt do mutacji
            secondPoint = selectedPath.get(secondNumber);                               //drugi punkt do mutacji
            //podmianka punktów
            selectedPath.remove(firstNumber);
            selectedPath.add(firstNumber, secondPoint);
            selectedPath.remove(secondNumber);
            selectedPath.add(secondNumber, firstPoint);
            selectedPath.add(0, startedPoint);                                     //dodanie wczesniej usuniętego punktu
            distance=calculatePath(selectedPath);
            list.add(new Path(distance, new ArrayList<Point>(selectedPath)));
        }


        Collections.sort(list, Comparator.comparing(Path::getDistance));
        for (int k=list.size(); k>population; k--){                                     //usuunięcie najbardziej niekorzystnych ścieżek
            list.remove(list.size()-1);
        }

        //wypisanie najlepszego wyniku z nowego pokolenia
        System.out.println("Wartość min w pokoleniu po mutacji:: " + list.get(0).getDistance());



        return list;

    }


    //roboczo, sprawdzenie czy sie dobrze zapisalo do listy
    public static void checkVisited(ArrayList<Point> loadList){
        for (int i=0; i<loadList.size();i++){
            System.out.println(loadList.get(i));
        }
    }
    //obliczenie dlugosci siezki
    public static double calculatePath(ArrayList<Point> path){
        double distance = 0;
        for (int i=0; i<path.size()-1;i++){
            distance+=distance(path.get(i).getX(),path.get(i).getY(),path.get(i+1).getX(),path.get(i+1).getY());
        }
        distance+= distance(path.get(0).getX(),path.get(0).getY(),path.get(path.size()-1).getX(),path.get(path.size()-1).getY());


        return distance;
    }
    //obliczenie dłogości odcinka między dwoma punktami
    public static double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    }
}
