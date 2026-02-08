CREATE DATABASE  IF NOT EXISTS `swimapp_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `swimapp_db`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: swimapp_db
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `istruttore`
--

DROP TABLE IF EXISTS `istruttore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `istruttore` (
  `email` varchar(45) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `istruttore`
--

LOCK TABLES `istruttore` WRITE;
/*!40000 ALTER TABLE `istruttore` DISABLE KEYS */;
INSERT INTO `istruttore` VALUES ('ercogoal22@gmail.com','Mattia','Ercolani'),('fedesici@gmail.com','fefo','sici'),('GregorioPaltrinieri@gmail.com','Gregorio','Paltrinieri'),('ilgemellodimaso@gmail.com','Matteo','Pinfildi'),('istruttore_1770134099683@test.it','null','null'),('istruttore_1770302474030@test.it','null','null'),('masoistruttore@gmail.com','maso','istruttore'),('michealphealps@gmail.com','Micheal','Phelps'),('simone.piras@gmail.com','Simone','Piras');
/*!40000 ALTER TABLE `istruttore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lezioni`
--

DROP TABLE IF EXISTS `lezioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lezioni` (
  `email` varchar(45) NOT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `cognome` varchar(45) DEFAULT NULL,
  `fascia_oraria` varchar(45) NOT NULL,
  `livello` varchar(45) DEFAULT NULL,
  `tariffa` float DEFAULT NULL,
  `tipo_lezione` varchar(45) DEFAULT NULL,
  `note` varchar(100) DEFAULT NULL,
  `giorni_disponibili` varchar(100) NOT NULL,
  PRIMARY KEY (`email`,`giorni_disponibili`,`fascia_oraria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lezioni`
--

LOCK TABLES `lezioni` WRITE;
/*!40000 ALTER TABLE `lezioni` DISABLE KEYS */;
INSERT INTO `lezioni` VALUES ('ercogoal22@gmail.com','Mattia','Ercolani','18-20','agonista',35,'privata','per campioni','martedì'),('fedesici@gmail.com','fefo','sici','14-16','intermedio',22.5,'in gruppo','Potenziamento gambe','giovedì'),('fedesici@gmail.com','fefo','sici','10-12','principiante',15.5,'privata','Portare cuffia','lunedì'),('fedesici@gmail.com','fefo','sici','16-18','avanzato',28,'in gruppo','Allenamento master','martedì'),('GregorioPaltrinieri@gmail.com','Gregorio','Paltrinieri','14-16','intermedio',20,'in gruppo','Uso piscina olimpionica','martedì'),('GregorioPaltrinieri@gmail.com','Gregorio','Paltrinieri','08-10','avanzato',40,'privata','Fondo e resistenza','sabato'),('ilgemellodimaso@gmail.com','Matteo','Pinfildi','09-11','avanzato',30,'in gruppo','Focus tecnica stile','lunedì'),('ilgemellodimaso@gmail.com','Matteo','Pinfildi','11-13','principiante',20,'in gruppo','Corso bambini','sabato'),('michealphealps@gmail.com','Micheal','Phelps','17-19','avanzato',50,'privata','Perfezionamento delfino','mercoledì'),('michealphealps@gmail.com','Micheal','Phelps','16-18','agonista',25,'privata','','venerdì'),('simone.piras@gmail.com','Simone','Piras','15-17','principiante',18,'privata','Ambientamento acqua','mercoledì'),('simone.piras@gmail.com','Simone','Piras','19-21','intermedio',25,'privata','Recupero post-infortunio','venerdì');
/*!40000 ALTER TABLE `lezioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prenotazione`
--

DROP TABLE IF EXISTS `prenotazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prenotazione` (
  `idprenotazione` int NOT NULL,
  `nomeIstruttore` varchar(45) DEFAULT NULL,
  `cognomeIstruttore` varchar(45) DEFAULT NULL,
  `mailutente` varchar(45) DEFAULT NULL,
  `mailistruttore` varchar(45) DEFAULT NULL,
  `prezzo` float DEFAULT NULL,
  `giorno` varchar(45) DEFAULT NULL,
  `info` varchar(45) DEFAULT NULL,
  `ora` float DEFAULT NULL,
  `status` enum('incorso','accettata','rifiutata') NOT NULL DEFAULT 'incorso',
  PRIMARY KEY (`idprenotazione`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenotazione`
--

LOCK TABLES `prenotazione` WRITE;
/*!40000 ALTER TABLE `prenotazione` DISABLE KEYS */;
INSERT INTO `prenotazione` VALUES (44,'Mattia','Ercolani','Leoboria11@gmail.com','ercogoal22@gmail.com',35,'martedì','per campioni',10,'incorso'),(46,'Mattia','Ercolani','admin@gmail.com','ercogoal22@gmail.com',35,'martedì','per campioni',19,'accettata'),(54,'fefo','sici','manucecchi@gmail.com','fedesici@gmail.com',15.5,'lunedì','Portare cuffia',10,'rifiutata');
/*!40000 ALTER TABLE `prenotazione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `Nome` varchar(45) DEFAULT NULL,
  `Cognome` varchar(45) DEFAULT NULL,
  `Email` varchar(100) NOT NULL,
  `IsIstructor` tinyint DEFAULT NULL,
  `Password` varchar(45) NOT NULL,
  PRIMARY KEY (`Email`,`Password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES ('Emaita','Italiani','admin@gmail.com',0,'admin1'),('Boromir','Gondor','Boromir@gmail.com',0,'Boromir2'),('Simone','Piras','elsimo2002@gmail.com',0,'Lelelegend'),('Emanuele','Italiani','emaita2002@gmail.com',0,'Simixleggend'),('Mattia','Ercolani','ercogoal@gmail.com',0,'xmattix'),('Mattia','Ercolani','ercogoal22@gmail.com',1,'xmattinn'),('Federico','Italiani','federicoitaliani16@gmail.com',0,'Fefo1601'),('fefo','sici','fedesici@gmail.com',1,'sicio02'),('Mateo','Pinfildi','gemellodimaso@gmail.com',0,'xrr40'),('Gregorio','Paltrinieri','GregorioPaltrinieri@gmail.com',1,'Greg02'),('Matteo','Pinfildi','ilgemellodimaso@gmail.com',1,'maso20'),('null','null','istruttore_1770134099683@test.it',1,'password123'),('null','null','istruttore_1770302474030@test.it',1,'password123'),('Jon','Snow','kingofthenorth02@gmail.com',0,'ramsaydislike'),('Emanuele','Italiani','lele@gmail.com',0,'Lelelele'),('Leo','Boria','Leoboria11@gmail.com',0,'Leoboriaa'),('Manuele','Cecchi','manucecchi@gmail.com',0,'manu01'),('Maso','Masetti','masini@gmail.com',1,'hatepinfo'),('matteo','maso','maso@gmail.com',1,'maso'),('maso','istruttore','masoistruttore@gmail.com',1,'maso02'),('Matteo','Masini','matteo.masini@email.it',0,'Masor20'),('1','Masini','matteomasini@gmail.com',0,'maso'),('Micheal','Phelps','michealphealps@gmail.com',1,'phelps02'),('pinfo','user','pinfouser@gmail.com',0,'pinfo'),('rick','dinno','rickdinno@gmail.com',1,'rick02'),('Roberto','Italiani','robertoitalico@gmail.com',0,'roberto67'),('Simone','Piras','simone.piras@gmail.com',1,'Simo102'),('Simone','Piras','simonepiras@gmail.com',0,'Simo102');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-08 13:40:31
