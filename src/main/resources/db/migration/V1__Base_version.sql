/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

set foreign_key_checks=0;

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `ID` int(11) NOT NULL,
  `STREET1` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `STREET2` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `CITY` varchar(50) COLLATE latin1_general_cs NOT NULL,
  `PROVINCE` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `POSTAL_CODE` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `EMAIL_ADDRESS` varchar(100) COLLATE latin1_general_cs DEFAULT NULL,
  `PHONE_NUMBER` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `FAX_NUMBER` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `COUNTRY` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aliquoted_specimen`
--

DROP TABLE IF EXISTS `aliquoted_specimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aliquoted_specimen` (
  `ID` int(11) NOT NULL,
  `QUANTITY` int(11) DEFAULT NULL,
  `VOLUME` decimal(20,10) DEFAULT NULL,
  `SPECIMEN_TYPE_ID` int(11) NOT NULL,
  `ACTIVITY_STATUS_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK75EACAC1F2A2464F` (`STUDY_ID`),
  KEY `FK75EACAC138445996` (`SPECIMEN_TYPE_ID`),
  CONSTRAINT `FK75EACAC138445996` FOREIGN KEY (`SPECIMEN_TYPE_ID`) REFERENCES `specimen_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK75EACAC1F2A2464F` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `batch_operation`
--

DROP TABLE IF EXISTS `batch_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_operation` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `TIME_EXECUTED` datetime NOT NULL,
  `EXECUTED_BY_USER_ID` int(11) NOT NULL,
  `FILE_DATA_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK5B242662F55E67FE` (`FILE_DATA_ID`),
  KEY `FK5B242662FB2C14CD` (`EXECUTED_BY_USER_ID`),
  CONSTRAINT `FK5B242662F55E67FE` FOREIGN KEY (`FILE_DATA_ID`) REFERENCES `file_data` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK5B242662FB2C14CD` FOREIGN KEY (`EXECUTED_BY_USER_ID`) REFERENCES `principal` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `batch_operation_event_attr`
--

DROP TABLE IF EXISTS `batch_operation_event_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_operation_event_attr` (
  `EVENT_ATTR_ID` int(11) NOT NULL,
  `BATCH_OPERATION_ID` int(11) NOT NULL,
  PRIMARY KEY (`EVENT_ATTR_ID`,`BATCH_OPERATION_ID`),
  KEY `FKF1184A93D3BA0590` (`BATCH_OPERATION_ID`),
  CONSTRAINT `FKF1184A93D3BA0590` FOREIGN KEY (`BATCH_OPERATION_ID`) REFERENCES `batch_operation` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `batch_operation_patient`
--

DROP TABLE IF EXISTS `batch_operation_patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_operation_patient` (
  `PROCESSING_EVENT_ID` int(11) NOT NULL,
  `BATCH_OPERATION_ID` int(11) NOT NULL,
  PRIMARY KEY (`PROCESSING_EVENT_ID`,`BATCH_OPERATION_ID`),
  KEY `FK69FFC208D3BA0590` (`BATCH_OPERATION_ID`),
  CONSTRAINT `FK69FFC208D3BA0590` FOREIGN KEY (`BATCH_OPERATION_ID`) REFERENCES `batch_operation` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `batch_operation_processing_event`
--

DROP TABLE IF EXISTS `batch_operation_processing_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_operation_processing_event` (
  `PROCESSING_EVENT_ID` int(11) NOT NULL,
  `BATCH_OPERATION_ID` int(11) NOT NULL,
  PRIMARY KEY (`PROCESSING_EVENT_ID`,`BATCH_OPERATION_ID`),
  KEY `FKDA49AA8BD3BA0590` (`BATCH_OPERATION_ID`),
  CONSTRAINT `FKDA49AA8BD3BA0590` FOREIGN KEY (`BATCH_OPERATION_ID`) REFERENCES `batch_operation` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `batch_operation_specimen`
--

DROP TABLE IF EXISTS `batch_operation_specimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_operation_specimen` (
  `SPECIMEN_ID` int(11) NOT NULL,
  `BATCH_OPERATION_ID` int(11) NOT NULL,
  PRIMARY KEY (`SPECIMEN_ID`,`BATCH_OPERATION_ID`),
  KEY `FKD2E0C45D3BA0590` (`BATCH_OPERATION_ID`),
  CONSTRAINT `FKD2E0C45D3BA0590` FOREIGN KEY (`BATCH_OPERATION_ID`) REFERENCES `batch_operation` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `center`
--

DROP TABLE IF EXISTS `center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `center` (
  `ID` int(11) NOT NULL,
  `DISCRIMINATOR` varchar(31) COLLATE latin1_general_cs NOT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `NAME_SHORT` varchar(50) COLLATE latin1_general_cs NOT NULL,
  `ADDRESS_ID` int(11) NOT NULL,
  `ACTIVITY_STATUS_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `SENDS_SHIPMENTS` tinyint(1) DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  UNIQUE KEY `NAME_SHORT` (`NAME_SHORT`),
  UNIQUE KEY `ADDRESS_ID` (`ADDRESS_ID`),
  UNIQUE KEY `STUDY_ID` (`STUDY_ID`),
  KEY `FK7645C055F2A2464F` (`STUDY_ID`),
  KEY `FK7645C0556AF2992F` (`ADDRESS_ID`),
  CONSTRAINT `FK7645C0556AF2992F` FOREIGN KEY (`ADDRESS_ID`) REFERENCES `address` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK7645C055F2A2464F` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `center_comment`
--

DROP TABLE IF EXISTS `center_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `center_comment` (
  `CENTER_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`CENTER_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FKDF3FBC55CDA9FD4F` (`COMMENT_ID`),
  KEY `FKDF3FBC5592FAA705` (`CENTER_ID`),
  CONSTRAINT `FKDF3FBC5592FAA705` FOREIGN KEY (`CENTER_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKDF3FBC55CDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collection_event`
--

DROP TABLE IF EXISTS `collection_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection_event` (
  `ID` int(11) NOT NULL,
  `VISIT_NUMBER` int(11) NOT NULL,
  `PATIENT_ID` int(11) NOT NULL,
  `ACTIVITY_STATUS_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PATIENT_ID` (`PATIENT_ID`,`VISIT_NUMBER`),
  KEY `FKEDAD8999B563F38F` (`PATIENT_ID`),
  CONSTRAINT `FKEDAD8999B563F38F` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collection_event_comment`
--

DROP TABLE IF EXISTS `collection_event_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection_event_comment` (
  `COLLECTION_EVENT_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`COLLECTION_EVENT_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FK1CFC0199280272F2` (`COLLECTION_EVENT_ID`),
  KEY `FK1CFC0199CDA9FD4F` (`COMMENT_ID`),
  CONSTRAINT `FK1CFC0199280272F2` FOREIGN KEY (`COLLECTION_EVENT_ID`) REFERENCES `collection_event` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK1CFC0199CDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `MESSAGE` text COLLATE latin1_general_cs NOT NULL,
  `CREATED_AT` datetime NOT NULL,
  `USER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK63717A3FB9634A05` (`USER_ID`),
  CONSTRAINT `FK63717A3FB9634A05` FOREIGN KEY (`USER_ID`) REFERENCES `principal` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(100) COLLATE latin1_general_cs NOT NULL,
  `TITLE` varchar(100) COLLATE latin1_general_cs DEFAULT NULL,
  `MOBILE_NUMBER` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `OFFICE_NUMBER` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `FAX_NUMBER` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `EMAIL_ADDRESS` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `PAGER_NUMBER` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `CLINIC_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6382B00057F87A25` (`CLINIC_ID`),
  CONSTRAINT `FK6382B00057F87A25` FOREIGN KEY (`CLINIC_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `container`
--

DROP TABLE IF EXISTS `container`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `container` (
  `ID` int(11) NOT NULL,
  `LABEL` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `PRODUCT_BARCODE` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `TEMPERATURE` double DEFAULT NULL,
  `CONTAINER_TYPE_ID` int(11) NOT NULL,
  `ACTIVITY_STATUS_ID` int(11) NOT NULL,
  `SITE_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `TOP_CONTAINER_ID` int(11) DEFAULT NULL,
  `PATH` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SITE_ID_2` (`SITE_ID`,`CONTAINER_TYPE_ID`,`LABEL`),
  UNIQUE KEY `SITE_ID` (`SITE_ID`,`PRODUCT_BARCODE`),
  KEY `FK8D995C613F52C885` (`SITE_ID`),
  KEY `PATH_IDX` (`PATH`),
  KEY `FK8D995C611BE0C379` (`TOP_CONTAINER_ID`),
  KEY `ID` (`ID`,`CONTAINER_TYPE_ID`),
  KEY `FK_Container_containerType` (`CONTAINER_TYPE_ID`,`SITE_ID`),
  CONSTRAINT `FK8D995C611BE0C379` FOREIGN KEY (`TOP_CONTAINER_ID`) REFERENCES `container` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK8D995C613F52C885` FOREIGN KEY (`SITE_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_Container_containerType` FOREIGN KEY (`CONTAINER_TYPE_ID`, `SITE_ID`) REFERENCES `container_type` (`ID`, `SITE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `container_comment`
--

DROP TABLE IF EXISTS `container_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `container_comment` (
  `CONTAINER_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`CONTAINER_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FK9A6C8C619BFD88CF` (`CONTAINER_ID`),
  KEY `FK9A6C8C61CDA9FD4F` (`COMMENT_ID`),
  CONSTRAINT `FK9A6C8C619BFD88CF` FOREIGN KEY (`CONTAINER_ID`) REFERENCES `container` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK9A6C8C61CDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `container_labeling_scheme`
--

DROP TABLE IF EXISTS `container_labeling_scheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `container_labeling_scheme` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `MIN_CHARS` int(11) DEFAULT NULL,
  `MAX_CHARS` int(11) DEFAULT NULL,
  `MAX_ROWS` int(11) DEFAULT NULL,
  `MAX_COLS` int(11) DEFAULT NULL,
  `MAX_CAPACITY` int(11) DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `HAS_MULTIPLE_LAYOUT` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `container_position`
--

DROP TABLE IF EXISTS `container_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `container_position` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `COL` int(11) NOT NULL,
  `ROW` int(11) NOT NULL,
  `PARENT_CONTAINER_ID` int(11) NOT NULL,
  `CONTAINER_ID` int(11) NOT NULL,
  `CONTAINER_TYPE_ID` int(11) NOT NULL,
  `PARENT_CONTAINER_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PARENT_CONTAINER_ID` (`PARENT_CONTAINER_ID`,`ROW`,`COL`),
  KEY `FK_ContainerPosition_container` (`CONTAINER_ID`,`CONTAINER_TYPE_ID`),
  KEY `FK_ContainerPosition_parentContainer` (`PARENT_CONTAINER_ID`,`PARENT_CONTAINER_TYPE_ID`),
  KEY `FK_ContainerPosition_containerTypeContainerType` (`PARENT_CONTAINER_TYPE_ID`,`CONTAINER_TYPE_ID`),
  CONSTRAINT `FK_ContainerPosition_container` FOREIGN KEY (`CONTAINER_ID`, `CONTAINER_TYPE_ID`) REFERENCES `container` (`ID`, `CONTAINER_TYPE_ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_ContainerPosition_containerTypeContainerType` FOREIGN KEY (`PARENT_CONTAINER_TYPE_ID`, `CONTAINER_TYPE_ID`) REFERENCES `container_type_container_type` (`PARENT_CONTAINER_TYPE_ID`, `CHILD_CONTAINER_TYPE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ContainerPosition_parentContainer` FOREIGN KEY (`PARENT_CONTAINER_ID`, `PARENT_CONTAINER_TYPE_ID`) REFERENCES `container` (`ID`, `CONTAINER_TYPE_ID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `container_type`
--

DROP TABLE IF EXISTS `container_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `container_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `NAME_SHORT` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `TOP_LEVEL` bit(1) DEFAULT NULL,
  `DEFAULT_TEMPERATURE` double DEFAULT NULL,
  `ACTIVITY_STATUS_ID` int(11) NOT NULL,
  `CHILD_LABELING_SCHEME_ID` int(11) NOT NULL,
  `SITE_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `COL_CAPACITY` int(11) NOT NULL,
  `ROW_CAPACITY` int(11) NOT NULL,
  `LABELING_LAYOUT` int(11) NOT NULL,
  `IS_MICROPLATE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SITE_ID_2` (`SITE_ID`,`NAME`),
  UNIQUE KEY `SITE_ID` (`SITE_ID`,`NAME_SHORT`),
  KEY `FKB2C878585D63DFF0` (`CHILD_LABELING_SCHEME_ID`),
  KEY `FKB2C878583F52C885` (`SITE_ID`),
  KEY `ID` (`ID`,`SITE_ID`),
  CONSTRAINT `FKB2C878583F52C885` FOREIGN KEY (`SITE_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKB2C878585D63DFF0` FOREIGN KEY (`CHILD_LABELING_SCHEME_ID`) REFERENCES `container_labeling_scheme` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `container_type_comment`
--

DROP TABLE IF EXISTS `container_type_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `container_type_comment` (
  `CONTAINER_TYPE_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`CONTAINER_TYPE_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FK6657C158B3E77A12` (`CONTAINER_TYPE_ID`),
  KEY `FK6657C158CDA9FD4F` (`COMMENT_ID`),
  CONSTRAINT `FK6657C158B3E77A12` FOREIGN KEY (`CONTAINER_TYPE_ID`) REFERENCES `container_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK6657C158CDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `container_type_container_type`
--

DROP TABLE IF EXISTS `container_type_container_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `container_type_container_type` (
  `PARENT_CONTAINER_TYPE_ID` int(11) NOT NULL,
  `CHILD_CONTAINER_TYPE_ID` int(11) NOT NULL,
  `SITE_ID` int(11) NOT NULL,
  PRIMARY KEY (`PARENT_CONTAINER_TYPE_ID`,`CHILD_CONTAINER_TYPE_ID`),
  KEY `FK_ContainerType_parentContainerTypes` (`PARENT_CONTAINER_TYPE_ID`,`SITE_ID`),
  KEY `FK_ContainerType_childContainerTypes` (`CHILD_CONTAINER_TYPE_ID`,`SITE_ID`),
  CONSTRAINT `FK_ContainerType_childContainerTypes` FOREIGN KEY (`CHILD_CONTAINER_TYPE_ID`, `SITE_ID`) REFERENCES `container_type` (`ID`, `SITE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ContainerType_parentContainerTypes` FOREIGN KEY (`PARENT_CONTAINER_TYPE_ID`, `SITE_ID`) REFERENCES `container_type` (`ID`, `SITE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `container_type_specimen_type`
--

DROP TABLE IF EXISTS `container_type_specimen_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `container_type_specimen_type` (
  `CONTAINER_TYPE_ID` int(11) NOT NULL,
  `SPECIMEN_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`CONTAINER_TYPE_ID`,`SPECIMEN_TYPE_ID`),
  KEY `FKE2F4C26AB3E77A12` (`CONTAINER_TYPE_ID`),
  KEY `FKE2F4C26A38445996` (`SPECIMEN_TYPE_ID`),
  CONSTRAINT `FKE2F4C26A38445996` FOREIGN KEY (`SPECIMEN_TYPE_ID`) REFERENCES `specimen_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKE2F4C26AB3E77A12` FOREIGN KEY (`CONTAINER_TYPE_ID`) REFERENCES `container_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_application`
--

DROP TABLE IF EXISTS `csm_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_application` (
  `APPLICATION_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `APPLICATION_NAME` varchar(255) NOT NULL,
  `APPLICATION_DESCRIPTION` varchar(200) NOT NULL,
  `DECLARATIVE_FLAG` tinyint(1) NOT NULL DEFAULT '0',
  `ACTIVE_FLAG` tinyint(1) NOT NULL DEFAULT '0',
  `UPDATE_DATE` date DEFAULT '0000-00-00',
  `DATABASE_URL` varchar(100) DEFAULT NULL,
  `DATABASE_USER_NAME` varchar(100) DEFAULT NULL,
  `DATABASE_PASSWORD` varchar(100) DEFAULT NULL,
  `DATABASE_DIALECT` varchar(100) DEFAULT NULL,
  `DATABASE_DRIVER` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`APPLICATION_ID`),
  UNIQUE KEY `UQ_APPLICATION_NAME` (`APPLICATION_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_filter_clause`
--

DROP TABLE IF EXISTS `csm_filter_clause`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_filter_clause` (
  `FILTER_CLAUSE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CLASS_NAME` varchar(100) NOT NULL,
  `FILTER_CHAIN` varchar(2000) NOT NULL,
  `TARGET_CLASS_NAME` varchar(100) NOT NULL,
  `TARGET_CLASS_ATTRIBUTE_NAME` varchar(100) NOT NULL,
  `TARGET_CLASS_ATTRIBUTE_TYPE` varchar(100) NOT NULL,
  `TARGET_CLASS_ALIAS` varchar(100) DEFAULT NULL,
  `TARGET_CLASS_ATTRIBUTE_ALIAS` varchar(100) DEFAULT NULL,
  `GENERATED_SQL_USER` varchar(4000) NOT NULL,
  `GENERATED_SQL_GROUP` varchar(4000) NOT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`FILTER_CLAUSE_ID`),
  KEY `FK_APPLICATION_FILTER_CLAUSE` (`APPLICATION_ID`),
  CONSTRAINT `FK_APPLICATION_FILTER_CLAUSE` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_group`
--

DROP TABLE IF EXISTS `csm_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_group` (
  `GROUP_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `GROUP_NAME` varchar(255) NOT NULL,
  `GROUP_DESC` varchar(200) DEFAULT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '0000-00-00',
  `APPLICATION_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`GROUP_ID`),
  UNIQUE KEY `UQ_GROUP_GROUP_NAME` (`APPLICATION_ID`,`GROUP_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_APPLICATION_GROUP` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_pg_pe`
--

DROP TABLE IF EXISTS `csm_pg_pe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_pg_pe` (
  `PG_PE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL,
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date DEFAULT '0000-00-00',
  PRIMARY KEY (`PG_PE_ID`),
  UNIQUE KEY `UQ_PROTECTION_GROUP_PROTECTION_ELEMENT_PROTECTION_GROUP_ID` (`PROTECTION_ELEMENT_ID`,`PROTECTION_GROUP_ID`),
  KEY `idx_PROTECTION_ELEMENT_ID` (`PROTECTION_ELEMENT_ID`),
  KEY `idx_PROTECTION_GROUP_ID` (`PROTECTION_GROUP_ID`),
  CONSTRAINT `FK_PROTECTION_ELEMENT_PROTECTION_GROUP` FOREIGN KEY (`PROTECTION_ELEMENT_ID`) REFERENCES `csm_protection_element` (`PROTECTION_ELEMENT_ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_PROTECTION_GROUP_PROTECTION_ELEMENT` FOREIGN KEY (`PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1530 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_privilege`
--

DROP TABLE IF EXISTS `csm_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_privilege` (
  `PRIVILEGE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRIVILEGE_NAME` varchar(100) NOT NULL,
  `PRIVILEGE_DESCRIPTION` varchar(200) DEFAULT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`PRIVILEGE_ID`),
  UNIQUE KEY `UQ_PRIVILEGE_NAME` (`PRIVILEGE_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_protection_element`
--

DROP TABLE IF EXISTS `csm_protection_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_protection_element` (
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PROTECTION_ELEMENT_NAME` varchar(100) NOT NULL,
  `PROTECTION_ELEMENT_DESCRIPTION` varchar(200) DEFAULT NULL,
  `OBJECT_ID` varchar(100) NOT NULL,
  `ATTRIBUTE` varchar(100) DEFAULT NULL,
  `ATTRIBUTE_VALUE` varchar(100) DEFAULT NULL,
  `PROTECTION_ELEMENT_TYPE` varchar(100) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`PROTECTION_ELEMENT_ID`),
  UNIQUE KEY `UQ_PE_PE_NAME_ATTRIBUTE_VALUE_APP_ID` (`OBJECT_ID`,`ATTRIBUTE`,`ATTRIBUTE_VALUE`,`APPLICATION_ID`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_PE_APPLICATION` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=303 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_protection_group`
--

DROP TABLE IF EXISTS `csm_protection_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_protection_group` (
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PROTECTION_GROUP_NAME` varchar(100) NOT NULL,
  `PROTECTION_GROUP_DESCRIPTION` varchar(200) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `LARGE_ELEMENT_COUNT_FLAG` tinyint(1) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '0000-00-00',
  `PARENT_PROTECTION_GROUP_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`PROTECTION_GROUP_ID`),
  UNIQUE KEY `UQ_PROTECTION_GROUP_PROTECTION_GROUP_NAME` (`APPLICATION_ID`,`PROTECTION_GROUP_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  KEY `idx_PARENT_PROTECTION_GROUP_ID` (`PARENT_PROTECTION_GROUP_ID`),
  CONSTRAINT `FK_PG_APPLICATION` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_PROTECTION_GROUP` FOREIGN KEY (`PARENT_PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_role`
--

DROP TABLE IF EXISTS `csm_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_role` (
  `ROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` varchar(100) NOT NULL,
  `ROLE_DESCRIPTION` varchar(200) DEFAULT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `ACTIVE_FLAG` tinyint(1) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`ROLE_ID`),
  UNIQUE KEY `UQ_ROLE_ROLE_NAME` (`APPLICATION_ID`,`ROLE_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_APPLICATION_ROLE` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_role_privilege`
--

DROP TABLE IF EXISTS `csm_role_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_role_privilege` (
  `ROLE_PRIVILEGE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` bigint(20) NOT NULL,
  `PRIVILEGE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ROLE_PRIVILEGE_ID`),
  UNIQUE KEY `UQ_ROLE_PRIVILEGE_ROLE_ID` (`PRIVILEGE_ID`,`ROLE_ID`),
  KEY `idx_PRIVILEGE_ID` (`PRIVILEGE_ID`),
  KEY `idx_ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `FK_PRIVILEGE_ROLE` FOREIGN KEY (`PRIVILEGE_ID`) REFERENCES `csm_privilege` (`PRIVILEGE_ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `csm_role` (`ROLE_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_user`
--

DROP TABLE IF EXISTS `csm_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_user` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LOGIN_NAME` varchar(500) NOT NULL,
  `MIGRATED_FLAG` tinyint(1) NOT NULL DEFAULT '0',
  `FIRST_NAME` varchar(100) NOT NULL,
  `LAST_NAME` varchar(100) NOT NULL,
  `ORGANIZATION` varchar(100) DEFAULT NULL,
  `DEPARTMENT` varchar(100) DEFAULT NULL,
  `TITLE` varchar(100) DEFAULT NULL,
  `PHONE_NUMBER` varchar(15) DEFAULT NULL,
  `PASSWORD` varchar(100) DEFAULT NULL,
  `EMAIL_ID` varchar(100) DEFAULT NULL,
  `START_DATE` date DEFAULT NULL,
  `END_DATE` date DEFAULT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '0000-00-00',
  `PREMGRT_LOGIN_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `UQ_LOGIN_NAME` (`LOGIN_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=410 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_user_group`
--

DROP TABLE IF EXISTS `csm_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_user_group` (
  `USER_GROUP_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL,
  `GROUP_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`USER_GROUP_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  KEY `idx_GROUP_ID` (`GROUP_ID`),
  CONSTRAINT `FK_UG_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `csm_group` (`GROUP_ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_USER_GROUP` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_user_group_role_pg`
--

DROP TABLE IF EXISTS `csm_user_group_role_pg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_user_group_role_pg` (
  `USER_GROUP_ROLE_PG_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) DEFAULT NULL,
  `GROUP_ID` bigint(20) DEFAULT NULL,
  `ROLE_ID` bigint(20) NOT NULL,
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`USER_GROUP_ROLE_PG_ID`),
  KEY `idx_GROUP_ID` (`GROUP_ID`),
  KEY `idx_ROLE_ID` (`ROLE_ID`),
  KEY `idx_PROTECTION_GROUP_ID` (`PROTECTION_GROUP_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_GROUPS` FOREIGN KEY (`GROUP_ID`) REFERENCES `csm_group` (`GROUP_ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_PROTECTION_GROUP` FOREIGN KEY (`PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `csm_role` (`ROLE_ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_USER` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=758 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csm_user_pe`
--

DROP TABLE IF EXISTS `csm_user_pe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csm_user_pe` (
  `USER_PROTECTION_ELEMENT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`USER_PROTECTION_ELEMENT_ID`),
  UNIQUE KEY `UQ_USER_PROTECTION_ELEMENT_PROTECTION_ELEMENT_ID` (`USER_ID`,`PROTECTION_ELEMENT_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  KEY `idx_PROTECTION_ELEMENT_ID` (`PROTECTION_ELEMENT_ID`),
  CONSTRAINT `FK_PE_USER` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_PROTECTION_ELEMENT_USER` FOREIGN KEY (`PROTECTION_ELEMENT_ID`) REFERENCES `csm_protection_element` (`PROTECTION_ELEMENT_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dispatch`
--

DROP TABLE IF EXISTS `dispatch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dispatch` (
  `ID` int(11) NOT NULL,
  `STATE` int(11) NOT NULL,
  `RECEIVER_CENTER_ID` int(11) NOT NULL,
  `SHIPMENT_INFO_ID` int(11) DEFAULT NULL,
  `SENDER_CENTER_ID` int(11) NOT NULL,
  `REQUEST_ID` int(11) DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SHIPMENT_INFO_ID` (`SHIPMENT_INFO_ID`),
  KEY `FK3F9F347A91BC3D7B` (`SENDER_CENTER_ID`),
  KEY `FK3F9F347AA2F14F4F` (`REQUEST_ID`),
  KEY `FK3F9F347A307B2CB5` (`RECEIVER_CENTER_ID`),
  KEY `FK3F9F347AF59D873A` (`SHIPMENT_INFO_ID`),
  CONSTRAINT `FK3F9F347A307B2CB5` FOREIGN KEY (`RECEIVER_CENTER_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK3F9F347A91BC3D7B` FOREIGN KEY (`SENDER_CENTER_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK3F9F347AA2F14F4F` FOREIGN KEY (`REQUEST_ID`) REFERENCES `request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK3F9F347AF59D873A` FOREIGN KEY (`SHIPMENT_INFO_ID`) REFERENCES `shipment_info` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dispatch_comment`
--

DROP TABLE IF EXISTS `dispatch_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dispatch_comment` (
  `DISPATCH_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`DISPATCH_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FKAFC93B7ACDA9FD4F` (`COMMENT_ID`),
  KEY `FKAFC93B7ADE99CA25` (`DISPATCH_ID`),
  CONSTRAINT `FKAFC93B7ACDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKAFC93B7ADE99CA25` FOREIGN KEY (`DISPATCH_ID`) REFERENCES `dispatch` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dispatch_specimen`
--

DROP TABLE IF EXISTS `dispatch_specimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dispatch_specimen` (
  `ID` int(11) NOT NULL,
  `STATE` int(11) NOT NULL,
  `SPECIMEN_ID` int(11) NOT NULL,
  `DISPATCH_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DISPATCH_ID` (`DISPATCH_ID`,`SPECIMEN_ID`),
  KEY `FKEE25592DEF199765` (`SPECIMEN_ID`),
  KEY `FKEE25592DDE99CA25` (`DISPATCH_ID`),
  CONSTRAINT `FKEE25592DDE99CA25` FOREIGN KEY (`DISPATCH_ID`) REFERENCES `dispatch` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKEE25592DEF199765` FOREIGN KEY (`SPECIMEN_ID`) REFERENCES `specimen` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dispatch_specimen_comment`
--

DROP TABLE IF EXISTS `dispatch_specimen_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dispatch_specimen_comment` (
  `DISPATCH_SPECIMEN_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`DISPATCH_SPECIMEN_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FKC3C4FD2DCDA9FD4F` (`COMMENT_ID`),
  KEY `FKC3C4FD2DBCCB06BA` (`DISPATCH_SPECIMEN_ID`),
  CONSTRAINT `FKC3C4FD2DBCCB06BA` FOREIGN KEY (`DISPATCH_SPECIMEN_ID`) REFERENCES `dispatch_specimen` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKC3C4FD2DCDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dna`
--

DROP TABLE IF EXISTS `dna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dna` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `SPECIMEN_ID` int(11) NOT NULL,
  `CONCENTRATION_ABS` decimal(20,10) DEFAULT NULL,
  `CONCENTRATION_FLUOR` decimal(20,10) DEFAULT NULL,
  `OD_260_OVER_280` decimal(20,10) DEFAULT NULL,
  `OD_260_OVER_230` decimal(20,10) DEFAULT NULL,
  `ALIQUOT_YIELD` decimal(20,10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SPECIMEN_ID` (`SPECIMEN_ID`),
  KEY `FK_Dna_Specimen` (`SPECIMEN_ID`),
  CONSTRAINT `FK_Dna_Specimen` FOREIGN KEY (`SPECIMEN_ID`) REFERENCES `specimen` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `domain`
--

DROP TABLE IF EXISTS `domain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `ALL_CENTERS` bit(1) DEFAULT NULL,
  `ALL_STUDIES` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `domain_center`
--

DROP TABLE IF EXISTS `domain_center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain_center` (
  `DOMAIN_ID` int(11) NOT NULL,
  `CENTER_ID` int(11) NOT NULL,
  PRIMARY KEY (`DOMAIN_ID`,`CENTER_ID`),
  KEY `FK8FE45030E3301CA5` (`DOMAIN_ID`),
  KEY `FK8FE4503092FAA705` (`CENTER_ID`),
  CONSTRAINT `FK8FE4503092FAA705` FOREIGN KEY (`CENTER_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK8FE45030E3301CA5` FOREIGN KEY (`DOMAIN_ID`) REFERENCES `domain` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `domain_study`
--

DROP TABLE IF EXISTS `domain_study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain_study` (
  `DOMAIN_ID` int(11) NOT NULL,
  `CENTER_ID` int(11) NOT NULL,
  PRIMARY KEY (`DOMAIN_ID`,`CENTER_ID`),
  KEY `FK816B9E6EE3301CA5` (`DOMAIN_ID`),
  KEY `FK816B9E6E5BB96C43` (`CENTER_ID`),
  CONSTRAINT `FK816B9E6E5BB96C43` FOREIGN KEY (`CENTER_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK816B9E6EE3301CA5` FOREIGN KEY (`DOMAIN_ID`) REFERENCES `domain` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `entity`
--

DROP TABLE IF EXISTS `entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity` (
  `ID` int(11) NOT NULL,
  `CLASS_NAME` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `entity_column`
--

DROP TABLE IF EXISTS `entity_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_column` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `ENTITY_PROPERTY_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK16BD7321698D6AC` (`ENTITY_PROPERTY_ID`),
  CONSTRAINT `FK16BD7321698D6AC` FOREIGN KEY (`ENTITY_PROPERTY_ID`) REFERENCES `entity_property` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `entity_filter`
--

DROP TABLE IF EXISTS `entity_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_filter` (
  `ID` int(11) NOT NULL,
  `FILTER_TYPE` int(11) DEFAULT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `ENTITY_PROPERTY_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK635CF541698D6AC` (`ENTITY_PROPERTY_ID`),
  CONSTRAINT `FK635CF541698D6AC` FOREIGN KEY (`ENTITY_PROPERTY_ID`) REFERENCES `entity_property` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `entity_property`
--

DROP TABLE IF EXISTS `entity_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_property` (
  `ID` int(11) NOT NULL,
  `PROPERTY` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `PROPERTY_TYPE_ID` int(11) NOT NULL,
  `ENTITY_ID` int(11) DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3FC956B191CFD445` (`ENTITY_ID`),
  KEY `FK3FC956B157C0C3B0` (`PROPERTY_TYPE_ID`),
  CONSTRAINT `FK3FC956B157C0C3B0` FOREIGN KEY (`PROPERTY_TYPE_ID`) REFERENCES `property_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK3FC956B191CFD445` FOREIGN KEY (`ENTITY_ID`) REFERENCES `entity` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_attr`
--

DROP TABLE IF EXISTS `event_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_attr` (
  `ID` int(11) NOT NULL,
  `VALUE` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `COLLECTION_EVENT_ID` int(11) NOT NULL,
  `STUDY_EVENT_ATTR_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK59508C96280272F2` (`COLLECTION_EVENT_ID`),
  KEY `FK59508C96A9CFCFDB` (`STUDY_EVENT_ATTR_ID`),
  CONSTRAINT `FK59508C96280272F2` FOREIGN KEY (`COLLECTION_EVENT_ID`) REFERENCES `collection_event` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK59508C96A9CFCFDB` FOREIGN KEY (`STUDY_EVENT_ATTR_ID`) REFERENCES `study_event_attr` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_attr_type`
--

DROP TABLE IF EXISTS `event_attr_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_attr_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(50) COLLATE latin1_general_cs NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_data`
--

DROP TABLE IF EXISTS `file_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_data` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `COMPRESSED_BYTES` longblob NOT NULL,
  `COMPRESSED_SIZE` bigint(20) NOT NULL,
  `FILE_META_DATA_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FILE_META_DATA_ID` (`FILE_META_DATA_ID`),
  KEY `FK595EC08DA965B18F` (`FILE_META_DATA_ID`),
  CONSTRAINT `FK595EC08DA965B18F` FOREIGN KEY (`FILE_META_DATA_ID`) REFERENCES `file_meta_data` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_meta_data`
--

DROP TABLE IF EXISTS `file_meta_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_meta_data` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `CONTENT_TYPE` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `CREATED_AT` datetime NOT NULL,
  `DESCRIPTION` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `MD5_HASH` binary(16) NOT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `SHA1_HASH` binary(20) NOT NULL,
  `SIZE` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `global_event_attr`
--

DROP TABLE IF EXISTS `global_event_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `global_event_attr` (
  `ID` int(11) NOT NULL,
  `LABEL` varchar(50) COLLATE latin1_general_cs NOT NULL,
  `EVENT_ATTR_TYPE_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `LABEL` (`LABEL`),
  KEY `FKBE7ED6B25B770B31` (`EVENT_ATTR_TYPE_ID`),
  CONSTRAINT `FKBE7ED6B25B770B31` FOREIGN KEY (`EVENT_ATTR_TYPE_ID`) REFERENCES `event_attr_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_user`
--

DROP TABLE IF EXISTS `group_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_user` (
  `GROUP_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  PRIMARY KEY (`GROUP_ID`,`USER_ID`),
  KEY `FK6B1EC1ABB9634A05` (`USER_ID`),
  KEY `FK6B1EC1ABA04C028F` (`GROUP_ID`),
  CONSTRAINT `FK6B1EC1ABA04C028F` FOREIGN KEY (`GROUP_ID`) REFERENCES `principal` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK6B1EC1ABB9634A05` FOREIGN KEY (`USER_ID`) REFERENCES `principal` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `next_val` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jasper_template`
--

DROP TABLE IF EXISTS `jasper_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jasper_template` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `NAME` varchar(50) COLLATE latin1_general_cs NOT NULL,
  `XML` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(100) COLLATE latin1_general_cs DEFAULT NULL,
  `CREATED_AT` datetime DEFAULT NULL,
  `CENTER` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `ACTION` varchar(100) COLLATE latin1_general_cs DEFAULT NULL,
  `PATIENT_NUMBER` varchar(100) COLLATE latin1_general_cs DEFAULT NULL,
  `INVENTORY_ID` varchar(100) COLLATE latin1_general_cs DEFAULT NULL,
  `LOCATION_LABEL` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `DETAILS` text COLLATE latin1_general_cs,
  `TYPE` varchar(100) COLLATE latin1_general_cs DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3061325 DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `membership`
--

DROP TABLE IF EXISTS `membership`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `membership` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `EVERY_PERMISSION` bit(1) DEFAULT NULL,
  `USER_MANAGER` bit(1) DEFAULT NULL,
  `DOMAIN_ID` int(11) NOT NULL,
  `PRINCIPAL_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DOMAIN_ID` (`DOMAIN_ID`),
  KEY `FKCD0773D6FF154DAF` (`PRINCIPAL_ID`),
  KEY `FKCD0773D6E3301CA5` (`DOMAIN_ID`),
  CONSTRAINT `FKCD0773D6E3301CA5` FOREIGN KEY (`DOMAIN_ID`) REFERENCES `domain` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKCD0773D6FF154DAF` FOREIGN KEY (`PRINCIPAL_ID`) REFERENCES `principal` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `membership_permission`
--

DROP TABLE IF EXISTS `membership_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `membership_permission` (
  `ID` int(11) NOT NULL,
  `PERMISSION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`,`PERMISSION_ID`),
  KEY `FK1350F1D815E6F8DC` (`ID`),
  CONSTRAINT `FK1350F1D815E6F8DC` FOREIGN KEY (`ID`) REFERENCES `membership` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `membership_role`
--

DROP TABLE IF EXISTS `membership_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `membership_role` (
  `MEMBERSHIP_ID` int(11) NOT NULL,
  `ROLE_ID` int(11) NOT NULL,
  PRIMARY KEY (`MEMBERSHIP_ID`,`ROLE_ID`),
  KEY `FKEF36B33F14388625` (`ROLE_ID`),
  KEY `FKEF36B33FD26ABDE5` (`MEMBERSHIP_ID`),
  CONSTRAINT `FKEF36B33F14388625` FOREIGN KEY (`ROLE_ID`) REFERENCES `role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKEF36B33FD26ABDE5` FOREIGN KEY (`MEMBERSHIP_ID`) REFERENCES `membership` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `origin_info`
--

DROP TABLE IF EXISTS `origin_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `origin_info` (
  `ID` int(11) NOT NULL,
  `SHIPMENT_INFO_ID` int(11) DEFAULT NULL,
  `CENTER_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `RECEIVER_SITE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SHIPMENT_INFO_ID` (`SHIPMENT_INFO_ID`),
  KEY `FKE92E7A2792FAA705` (`CENTER_ID`),
  KEY `FKE92E7A27F59D873A` (`SHIPMENT_INFO_ID`),
  KEY `FKE92E7A274D7A8883` (`RECEIVER_SITE_ID`),
  CONSTRAINT `FKE92E7A274D7A8883` FOREIGN KEY (`RECEIVER_SITE_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKE92E7A2792FAA705` FOREIGN KEY (`CENTER_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKE92E7A27F59D873A` FOREIGN KEY (`SHIPMENT_INFO_ID`) REFERENCES `shipment_info` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `origin_info_comment`
--

DROP TABLE IF EXISTS `origin_info_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `origin_info_comment` (
  `ORIGIN_INFO_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`ORIGIN_INFO_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FKFE82842712E55F12` (`ORIGIN_INFO_ID`),
  KEY `FKFE828427CDA9FD4F` (`COMMENT_ID`),
  CONSTRAINT `FKFE82842712E55F12` FOREIGN KEY (`ORIGIN_INFO_ID`) REFERENCES `origin_info` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKFE828427CDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `ID` int(11) NOT NULL,
  `PNUMBER` varchar(100) COLLATE latin1_general_cs NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `CREATED_AT` datetime NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PNUMBER` (`PNUMBER`),
  KEY `FKFB9F76E5F2A2464F` (`STUDY_ID`),
  KEY `NUMBER_IDX` (`PNUMBER`),
  CONSTRAINT `FKFB9F76E5F2A2464F` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `patient_comment`
--

DROP TABLE IF EXISTS `patient_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_comment` (
  `PATIENT_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`PATIENT_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FK901E2E5B563F38F` (`PATIENT_ID`),
  KEY `FK901E2E5CDA9FD4F` (`COMMENT_ID`),
  CONSTRAINT `FK901E2E5B563F38F` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK901E2E5CDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `principal`
--

DROP TABLE IF EXISTS `principal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `principal` (
  `DISCRIMINATOR` varchar(31) COLLATE latin1_general_cs NOT NULL,
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `ACTIVITY_STATUS_ID` int(11) NOT NULL,
  `DESCRIPTION` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `CSM_USER_ID` bigint(20) DEFAULT NULL,
  `EMAIL` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `FULL_NAME` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `LOGIN` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `NEED_PWD_CHANGE` bit(1) DEFAULT NULL,
  `RECV_BULK_EMAILS` bit(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  UNIQUE KEY `CSM_USER_ID` (`CSM_USER_ID`),
  UNIQUE KEY `EMAIL` (`EMAIL`),
  UNIQUE KEY `LOGIN` (`LOGIN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `printed_ss_inv_item`
--

DROP TABLE IF EXISTS `printed_ss_inv_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `printed_ss_inv_item` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `TXT` varchar(15) COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TXT` (`TXT`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `printer_label_template`
--

DROP TABLE IF EXISTS `printer_label_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `printer_label_template` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `NAME` varchar(50) COLLATE latin1_general_cs NOT NULL,
  `PRINTER_NAME` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `CONFIG_DATA` text COLLATE latin1_general_cs,
  `JASPER_TEMPLATE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FKC6463C6AA4B878C8` (`JASPER_TEMPLATE_ID`),
  CONSTRAINT `FKC6463C6AA4B878C8` FOREIGN KEY (`JASPER_TEMPLATE_ID`) REFERENCES `jasper_template` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `processing_event`
--

DROP TABLE IF EXISTS `processing_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `processing_event` (
  `ID` int(11) NOT NULL,
  `WORKSHEET` varchar(150) COLLATE latin1_general_cs NOT NULL,
  `CREATED_AT` datetime NOT NULL,
  `CENTER_ID` int(11) NOT NULL,
  `ACTIVITY_STATUS_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `PROCESSED_BY` varchar(63) COLLATE latin1_general_cs DEFAULT NULL,
  `PERSON_USER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `WORKSHEET` (`WORKSHEET`),
  KEY `CREATED_AT_IDX` (`CREATED_AT`),
  KEY `FK327B1E4E92FAA705` (`CENTER_ID`),
  KEY `FK327B1E4E21C4671B` (`PERSON_USER_ID`),
  CONSTRAINT `FK327B1E4E21C4671B` FOREIGN KEY (`PERSON_USER_ID`) REFERENCES `principal` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK327B1E4E92FAA705` FOREIGN KEY (`CENTER_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `processing_event_comment`
--

DROP TABLE IF EXISTS `processing_event_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `processing_event_comment` (
  `PROCESSING_EVENT_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`PROCESSING_EVENT_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FKA958114E33126C8` (`PROCESSING_EVENT_ID`),
  KEY `FKA958114ECDA9FD4F` (`COMMENT_ID`),
  CONSTRAINT `FKA958114E33126C8` FOREIGN KEY (`PROCESSING_EVENT_ID`) REFERENCES `processing_event` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKA958114ECDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `property_modifier`
--

DROP TABLE IF EXISTS `property_modifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property_modifier` (
  `ID` int(11) NOT NULL,
  `NAME` text COLLATE latin1_general_cs,
  `PROPERTY_MODIFIER` text COLLATE latin1_general_cs,
  `PROPERTY_TYPE_ID` int(11) DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK5DF9160157C0C3B0` (`PROPERTY_TYPE_ID`),
  CONSTRAINT `FK5DF9160157C0C3B0` FOREIGN KEY (`PROPERTY_TYPE_ID`) REFERENCES `property_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `property_type`
--

DROP TABLE IF EXISTS `property_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `DESCRIPTION` text COLLATE latin1_general_cs,
  `USER_ID` int(11) NOT NULL,
  `IS_PUBLIC` bit(1) NOT NULL,
  `IS_COUNT` bit(1) NOT NULL,
  `ENTITY_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK8FDF493491CFD445` (`ENTITY_ID`),
  KEY `FK8FDF4934B9634A05` (`USER_ID`),
  CONSTRAINT `FK8FDF493491CFD445` FOREIGN KEY (`ENTITY_ID`) REFERENCES `entity` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK8FDF4934B9634A05` FOREIGN KEY (`USER_ID`) REFERENCES `principal` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_column`
--

DROP TABLE IF EXISTS `report_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_column` (
  `ID` int(11) NOT NULL,
  `POSITION` int(11) DEFAULT NULL,
  `COLUMN_ID` int(11) NOT NULL,
  `PROPERTY_MODIFIER_ID` int(11) DEFAULT NULL,
  `REPORT_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKF0B78C1BE9306A5` (`REPORT_ID`),
  KEY `FKF0B78C1C2DE3790` (`PROPERTY_MODIFIER_ID`),
  KEY `FKF0B78C1A946D8E8` (`COLUMN_ID`),
  CONSTRAINT `FKF0B78C1A946D8E8` FOREIGN KEY (`COLUMN_ID`) REFERENCES `entity_column` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKF0B78C1BE9306A5` FOREIGN KEY (`REPORT_ID`) REFERENCES `report` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKF0B78C1C2DE3790` FOREIGN KEY (`PROPERTY_MODIFIER_ID`) REFERENCES `property_modifier` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_filter`
--

DROP TABLE IF EXISTS `report_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_filter` (
  `ID` int(11) NOT NULL,
  `POSITION` int(11) DEFAULT NULL,
  `OPERATOR` int(11) DEFAULT NULL,
  `ENTITY_FILTER_ID` int(11) NOT NULL,
  `REPORT_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK13D570E3445CEC4C` (`ENTITY_FILTER_ID`),
  KEY `FK13D570E3BE9306A5` (`REPORT_ID`),
  CONSTRAINT `FK13D570E3445CEC4C` FOREIGN KEY (`ENTITY_FILTER_ID`) REFERENCES `entity_filter` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK13D570E3BE9306A5` FOREIGN KEY (`REPORT_ID`) REFERENCES `report` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_filter_value`
--

DROP TABLE IF EXISTS `report_filter_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_filter_value` (
  `ID` int(11) NOT NULL,
  `POSITION` int(11) DEFAULT NULL,
  `VALUE` text COLLATE latin1_general_cs,
  `SECOND_VALUE` text COLLATE latin1_general_cs,
  `REPORT_FILTER_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK691EF6F59FFD1CEE` (`REPORT_FILTER_ID`),
  CONSTRAINT `FK691EF6F59FFD1CEE` FOREIGN KEY (`REPORT_FILTER_ID`) REFERENCES `report_filter` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request` (
  `ID` int(11) NOT NULL,
  `SUBMITTED` datetime DEFAULT NULL,
  `CREATED` datetime NOT NULL,
  `ADDRESS_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `RESEARCH_GROUP_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6C1A7E6F6AF2992F` (`ADDRESS_ID`),
  KEY `FK6C1A7E6F4BD922D8` (`RESEARCH_GROUP_ID`),
  CONSTRAINT `FK6C1A7E6F4BD922D8` FOREIGN KEY (`RESEARCH_GROUP_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK6C1A7E6F6AF2992F` FOREIGN KEY (`ADDRESS_ID`) REFERENCES `address` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `request_specimen`
--

DROP TABLE IF EXISTS `request_specimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request_specimen` (
  `ID` int(11) NOT NULL,
  `STATE` int(11) NOT NULL,
  `CLAIMED_BY` varchar(50) COLLATE latin1_general_cs DEFAULT NULL,
  `SPECIMEN_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `REQUEST_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK579572D8EF199765` (`SPECIMEN_ID`),
  KEY `FK579572D8A2F14F4F` (`REQUEST_ID`),
  CONSTRAINT `FK579572D8A2F14F4F` FOREIGN KEY (`REQUEST_ID`) REFERENCES `request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK579572D8EF199765` FOREIGN KEY (`SPECIMEN_ID`) REFERENCES `specimen` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `revision`
--

DROP TABLE IF EXISTS `revision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `revision` (
  `ID` int(11) NOT NULL,
  `CREATED_AT` datetime DEFAULT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK1F1AA7DBB9634A05` (`USER_ID`),
  CONSTRAINT `FK1F1AA7DBB9634A05` FOREIGN KEY (`USER_ID`) REFERENCES `principal` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `revision_entity_type`
--

DROP TABLE IF EXISTS `revision_entity_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `revision_entity_type` (
  `ID` int(11) NOT NULL,
  `TYPE` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `REVISION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK74D6F3B29D2D0285` (`REVISION_ID`),
  CONSTRAINT `FK74D6F3B29D2D0285` FOREIGN KEY (`REVISION_ID`) REFERENCES `revision` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `ID` int(11) NOT NULL,
  `PERMISSION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`,`PERMISSION_ID`),
  KEY `FK9C6EC938C226FDBC` (`ID`),
  CONSTRAINT `FK9C6EC938C226FDBC` FOREIGN KEY (`ID`) REFERENCES `role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `schema_version`
--

DROP TABLE IF EXISTS `schema_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schema_version` (
  `version_rank` int(11) NOT NULL,
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(30) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  KEY `schema_version_vr_idx` (`version_rank`),
  KEY `schema_version_ir_idx` (`installed_rank`),
  KEY `schema_version_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shipment_info`
--

DROP TABLE IF EXISTS `shipment_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shipment_info` (
  `ID` int(11) NOT NULL,
  `RECEIVED_AT` datetime DEFAULT NULL,
  `PACKED_AT` datetime DEFAULT NULL,
  `WAYBILL` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `BOX_NUMBER` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `SHIPPING_METHOD_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK95BCA433DCA49682` (`SHIPPING_METHOD_ID`),
  KEY `WAYBILL_IDX` (`WAYBILL`),
  KEY `RECEIVED_AT_IDX` (`RECEIVED_AT`),
  CONSTRAINT `FK95BCA433DCA49682` FOREIGN KEY (`SHIPPING_METHOD_ID`) REFERENCES `shipping_method` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shipping_method`
--

DROP TABLE IF EXISTS `shipping_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shipping_method` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `site_study`
--

DROP TABLE IF EXISTS `site_study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_study` (
  `STUDY_ID` int(11) NOT NULL DEFAULT '0',
  `SITE_ID` int(11) NOT NULL,
  PRIMARY KEY (`SITE_ID`,`STUDY_ID`),
  KEY `FK7A197EB1F2A2464F` (`STUDY_ID`),
  KEY `FK7A197EB13F52C885` (`SITE_ID`),
  CONSTRAINT `FK7A197EB13F52C885` FOREIGN KEY (`SITE_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK7A197EB1F2A2464F` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `source_specimen`
--

DROP TABLE IF EXISTS `source_specimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `source_specimen` (
  `ID` int(11) NOT NULL,
  `NEED_ORIGINAL_VOLUME` tinyint(1) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `SPECIMEN_TYPE_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK28D36ACF2A2464F` (`STUDY_ID`),
  KEY `FK28D36AC38445996` (`SPECIMEN_TYPE_ID`),
  CONSTRAINT `FK28D36AC38445996` FOREIGN KEY (`SPECIMEN_TYPE_ID`) REFERENCES `specimen_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK28D36ACF2A2464F` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `specimen`
--

DROP TABLE IF EXISTS `specimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specimen` (
  `ID` int(11) NOT NULL,
  `INVENTORY_ID` varchar(100) COLLATE latin1_general_cs NOT NULL,
  `QUANTITY` decimal(20,10) DEFAULT NULL,
  `CREATED_AT` datetime NOT NULL,
  `ACTIVITY_STATUS_ID` int(11) NOT NULL,
  `ORIGINAL_COLLECTION_EVENT_ID` int(11) DEFAULT NULL,
  `PROCESSING_EVENT_ID` int(11) DEFAULT NULL,
  `ORIGIN_INFO_ID` int(11) NOT NULL,
  `SPECIMEN_TYPE_ID` int(11) NOT NULL,
  `COLLECTION_EVENT_ID` int(11) NOT NULL,
  `PARENT_SPECIMEN_ID` int(11) DEFAULT NULL,
  `TOP_SPECIMEN_ID` int(11) DEFAULT NULL,
  `CURRENT_CENTER_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `PLATE_ERRORS` text COLLATE latin1_general_cs,
  `SAMPLE_ERRORS` text COLLATE latin1_general_cs,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `INVENTORY_ID` (`INVENTORY_ID`),
  KEY `FKAF84F30886857784` (`ORIGINAL_COLLECTION_EVENT_ID`),
  KEY `FKAF84F308280272F2` (`COLLECTION_EVENT_ID`),
  KEY `FKAF84F30812E55F12` (`ORIGIN_INFO_ID`),
  KEY `FKAF84F30861674F50` (`PARENT_SPECIMEN_ID`),
  KEY `FKAF84F30833126C8` (`PROCESSING_EVENT_ID`),
  KEY `FKAF84F308FBB79BBF` (`CURRENT_CENTER_ID`),
  KEY `FKAF84F30838445996` (`SPECIMEN_TYPE_ID`),
  KEY `FKAF84F308C9EF5F7B` (`TOP_SPECIMEN_ID`),
  KEY `ID` (`ID`,`SPECIMEN_TYPE_ID`),
  CONSTRAINT `FKAF84F30812E55F12` FOREIGN KEY (`ORIGIN_INFO_ID`) REFERENCES `origin_info` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKAF84F308280272F2` FOREIGN KEY (`COLLECTION_EVENT_ID`) REFERENCES `collection_event` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKAF84F30833126C8` FOREIGN KEY (`PROCESSING_EVENT_ID`) REFERENCES `processing_event` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKAF84F30838445996` FOREIGN KEY (`SPECIMEN_TYPE_ID`) REFERENCES `specimen_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKAF84F30861674F50` FOREIGN KEY (`PARENT_SPECIMEN_ID`) REFERENCES `specimen` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKAF84F30886857784` FOREIGN KEY (`ORIGINAL_COLLECTION_EVENT_ID`) REFERENCES `collection_event` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKAF84F308C9EF5F7B` FOREIGN KEY (`TOP_SPECIMEN_ID`) REFERENCES `specimen` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKAF84F308FBB79BBF` FOREIGN KEY (`CURRENT_CENTER_ID`) REFERENCES `center` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `specimen_comment`
--

DROP TABLE IF EXISTS `specimen_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specimen_comment` (
  `SPECIMEN_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`SPECIMEN_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FK73068C08EF199765` (`SPECIMEN_ID`),
  KEY `FK73068C08CDA9FD4F` (`COMMENT_ID`),
  CONSTRAINT `FK73068C08CDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK73068C08EF199765` FOREIGN KEY (`SPECIMEN_ID`) REFERENCES `specimen` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `specimen_position`
--

DROP TABLE IF EXISTS `specimen_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specimen_position` (
  `ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `COL` int(11) NOT NULL,
  `ROW` int(11) NOT NULL,
  `POSITION_STRING` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `CONTAINER_ID` int(11) NOT NULL,
  `SPECIMEN_ID` int(11) NOT NULL,
  `CONTAINER_TYPE_ID` int(11) NOT NULL,
  `SPECIMEN_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CONTAINER_ID` (`CONTAINER_ID`,`ROW`,`COL`),
  UNIQUE KEY `SPECIMEN_ID` (`SPECIMEN_ID`),
  KEY `FK_SpecimenPosition_containerTypeSpecimenType` (`CONTAINER_TYPE_ID`,`SPECIMEN_TYPE_ID`),
  KEY `FK_SpecimenPosition_container` (`CONTAINER_ID`,`CONTAINER_TYPE_ID`),
  KEY `FK_SpecimenPosition_specimen` (`SPECIMEN_ID`,`SPECIMEN_TYPE_ID`),
  CONSTRAINT `FK_SpecimenPosition_container` FOREIGN KEY (`CONTAINER_ID`, `CONTAINER_TYPE_ID`) REFERENCES `container` (`ID`, `CONTAINER_TYPE_ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_SpecimenPosition_containerTypeSpecimenType` FOREIGN KEY (`CONTAINER_TYPE_ID`, `SPECIMEN_TYPE_ID`) REFERENCES `container_type_specimen_type` (`CONTAINER_TYPE_ID`, `SPECIMEN_TYPE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_SpecimenPosition_specimen` FOREIGN KEY (`SPECIMEN_ID`, `SPECIMEN_TYPE_ID`) REFERENCES `specimen` (`ID`, `SPECIMEN_TYPE_ID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `specimen_type`
--

DROP TABLE IF EXISTS `specimen_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specimen_type` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `NAME_SHORT` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  UNIQUE KEY `NAME_SHORT` (`NAME_SHORT`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `specimen_type_specimen_type`
--

DROP TABLE IF EXISTS `specimen_type_specimen_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specimen_type_specimen_type` (
  `CHILD_SPECIMEN_TYPE_ID` int(11) NOT NULL,
  `PARENT_SPECIMEN_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`PARENT_SPECIMEN_TYPE_ID`,`CHILD_SPECIMEN_TYPE_ID`),
  KEY `FKD95844635F3DC8B` (`PARENT_SPECIMEN_TYPE_ID`),
  KEY `FKD9584463D9672259` (`CHILD_SPECIMEN_TYPE_ID`),
  CONSTRAINT `FKD95844635F3DC8B` FOREIGN KEY (`PARENT_SPECIMEN_TYPE_ID`) REFERENCES `specimen_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKD9584463D9672259` FOREIGN KEY (`CHILD_SPECIMEN_TYPE_ID`) REFERENCES `specimen_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study`
--

DROP TABLE IF EXISTS `study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) COLLATE latin1_general_cs NOT NULL,
  `NAME_SHORT` varchar(50) COLLATE latin1_general_cs NOT NULL,
  `ACTIVITY_STATUS_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  UNIQUE KEY `NAME_SHORT` (`NAME_SHORT`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_comment`
--

DROP TABLE IF EXISTS `study_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_comment` (
  `STUDY_ID` int(11) NOT NULL,
  `COMMENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`STUDY_ID`,`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FKAA027DA9F2A2464F` (`STUDY_ID`),
  KEY `FKAA027DA9CDA9FD4F` (`COMMENT_ID`),
  CONSTRAINT `FKAA027DA9CDA9FD4F` FOREIGN KEY (`COMMENT_ID`) REFERENCES `comment` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKAA027DA9F2A2464F` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_contact`
--

DROP TABLE IF EXISTS `study_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_contact` (
  `STUDY_ID` int(11) NOT NULL,
  `CONTACT_ID` int(11) NOT NULL,
  PRIMARY KEY (`STUDY_ID`,`CONTACT_ID`),
  KEY `FKAA13B36AF2A2464F` (`STUDY_ID`),
  KEY `FKAA13B36AA07999AF` (`CONTACT_ID`),
  CONSTRAINT `FKAA13B36AA07999AF` FOREIGN KEY (`CONTACT_ID`) REFERENCES `contact` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKAA13B36AF2A2464F` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_event_attr`
--

DROP TABLE IF EXISTS `study_event_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_event_attr` (
  `ID` int(11) NOT NULL,
  `PERMISSIBLE` varchar(255) COLLATE latin1_general_cs DEFAULT NULL,
  `REQUIRED` bit(1) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `ACTIVITY_STATUS_ID` int(11) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `GLOBAL_EVENT_ATTR_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3EACD8ECF2A2464F` (`STUDY_ID`),
  KEY `FK3EACD8EC44556025` (`GLOBAL_EVENT_ATTR_ID`),
  CONSTRAINT `FK3EACD8EC44556025` FOREIGN KEY (`GLOBAL_EVENT_ATTR_ID`) REFERENCES `global_event_attr` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK3EACD8ECF2A2464F` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO `principal` VALUES
('User',1,0,1,NULL,'',1,'','Administrator','admin','\0','\0'),
('BbGroup',2,1,1,'Global Administrators','Global Administrators',NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO group_user(user_id, group_id) VALUES (1,2);

INSERT INTO `domain` VALUES
(1,0,b'1',b'1');

INSERT INTO `membership` VALUES
(1,0,b'1',b'1',1,2);

INSERT INTO `role` VALUES
(1,1,'Technician 2'),
(2,1,'Technician 1'),
(3,1,'Collection Event + Dispatch'),
(5,1,'Read Only'),
(6,1,'Study & Site Coordinator');

INSERT INTO `csm_application` VALUES
       (1,'csmupt','CSM UPT Super Admin Application',0,0,'2009-07-22',NULL,NULL,NULL,NULL,NULL),
       (2,'biobank','biobank',0,0,'2009-07-22','jdbc:mysql://localhost:3306/biobank','dummy','4UlzrQJztJY=','org.hibernate.dialect.MySQLDialect','com.mysql.jdbc.Driver');

INSERT INTO csm_user VALUES
       (1,'admin',0,'Administrator','NoName',NULL,NULL,NULL,NULL,'orDBlaojDQE=',NULL,NULL,NULL,'2009-07-22',NULL);

INSERT INTO `csm_protection_element` VALUES
(1,'csmupt','CSM UPT Super Admin Application Protection Element','csmupt',NULL,NULL,NULL,1,'2009-07-22'),
(2,'biobank','biobank','biobank',NULL,NULL,NULL,1,'2009-07-22'),
(3,'edu.ualberta.med.biobank.model.ActivityStatus','edu.ualberta.med.biobank.model.ActivityStatus','edu.ualberta.med.biobank.model.ActivityStatus','','','',2,'2010-03-04'),
(4,'edu.ualberta.med.biobank.model.Address','edu.ualberta.med.biobank.model.Address','edu.ualberta.med.biobank.model.Address',NULL,NULL,NULL,2,'2009-07-22'),
(5,'edu.ualberta.med.biobank.model.Capacity','edu.ualberta.med.biobank.model.Capacity','edu.ualberta.med.biobank.model.Capacity',NULL,NULL,NULL,2,'2009-07-22'),
(6,'edu.ualberta.med.biobank.model.Clinic','edu.ualberta.med.biobank.model.Clinic','edu.ualberta.med.biobank.model.Clinic',NULL,NULL,NULL,2,'2009-07-22'),
(7,'edu.ualberta.med.biobank.model.ContainerPosition','edu.ualberta.med.biobank.model.ContainerPosition','edu.ualberta.med.biobank.model.ContainerPosition',NULL,NULL,NULL,2,'2009-07-22'),
(8,'edu.ualberta.med.biobank.model.Patient','edu.ualberta.med.biobank.model.Patient','edu.ualberta.med.biobank.model.Patient',NULL,NULL,NULL,2,'2009-07-22'),
(10,'edu.ualberta.med.biobank.model.StudyEventAttr','edu.ualberta.med.biobank.model.StudyEventAttr','edu.ualberta.med.biobank.model.StudyEventAttr','','','',2,'2011-02-28'),
(11,'edu.ualberta.med.biobank.model.EventAttr','edu.ualberta.med.biobank.model.EventAttr','edu.ualberta.med.biobank.model.EventAttr','','','',2,'2011-02-28'),
(12,'edu.ualberta.med.biobank.model.GlobalEventAttr','edu.ualberta.med.biobank.model.GlobalEventAttr','edu.ualberta.med.biobank.model.GlobalEventAttr','','','',2,'2011-02-28'),
(13,'edu.ualberta.med.biobank.model.EventAttrType','edu.ualberta.med.biobank.model.EventAttrType','edu.ualberta.med.biobank.model.EventAttrType','','','',2,'2011-02-28'),
(15,'edu.ualberta.med.biobank.model.SpecimenPosition','edu.ualberta.med.biobank.model.SpecimenPosition','edu.ualberta.med.biobank.model.SpecimenPosition','','','',2,'2011-02-28'),
(16,'edu.ualberta.med.biobank.model.OriginInfo','edu.ualberta.med.biobank.model.OriginInfo','edu.ualberta.med.biobank.model.OriginInfo','','','',2,'2011-02-28'),
(18,'edu.ualberta.med.biobank.model.Site','edu.ualberta.med.biobank.model.Site','edu.ualberta.med.biobank.model.Site',NULL,NULL,NULL,2,'2009-07-22'),
(19,'edu.ualberta.med.biobank.model.Container','edu.ualberta.med.biobank.model.Container','edu.ualberta.med.biobank.model.Container','','','',2,'2010-08-19'),
(20,'edu.ualberta.med.biobank.model.ContainerType','edu.ualberta.med.biobank.model.ContainerType','edu.ualberta.med.biobank.model.ContainerType',NULL,NULL,NULL,2,'2009-07-22'),
(21,'edu.ualberta.med.biobank.model.Study','edu.ualberta.med.biobank.model.Study','edu.ualberta.med.biobank.model.Study',NULL,NULL,NULL,2,'2009-07-22'),
(24,'edu.ualberta.med.biobank.model.Specimen','edu.ualberta.med.biobank.model.Specimen','edu.ualberta.med.biobank.model.Specimen','','','',2,'2011-02-28'),
(25,'edu.ualberta.med.biobank.model.AliquotedSpecimen','edu.ualberta.med.biobank.model.AliquotedSpecimen','edu.ualberta.med.biobank.model.AliquotedSpecimen','','','',2,'2011-02-28'),
(27,'edu.ualberta.med.biobank.model.ContainerLabelingScheme','edu.ualberta.med.biobank.model.ContainerLabelingScheme','edu.ualberta.med.biobank.model.ContainerLabelingScheme','','','',2,'2009-07-26'),
(30,'edu.ualberta.med.biobank.model.Contact','edu.ualberta.med.biobank.model.Contact','edu.ualberta.med.biobank.model.Contact','','','',2,'2009-08-24'),
(32,'edu.ualberta.med.biobank.model.ShippingMethod','edu.ualberta.med.biobank.model.ShippingMethod','edu.ualberta.med.biobank.model.ShippingMethod','','','',2,'2009-11-30'),
(35,'edu.ualberta.med.biobank.model.SourceSpecimen','edu.ualberta.med.biobank.model.SourceSpecimen','edu.ualberta.med.biobank.model.SourceSpecimen','','','',2,'2011-02-28'),
(36,'edu.ualberta.med.biobank.model.AbstractPosition','','edu.ualberta.med.biobank.model.AbstractPosition','','','',2,'2010-03-15'),
(51,'edu.ualberta.med.biobank.model.Log','','edu.ualberta.med.biobank.model.Log','','','',2,'2010-05-25'),
(65,'edu.ualberta.med.biobank.model.Dispatch','edu.ualberta.med.biobank.model.Dispatch','edu.ualberta.med.biobank.model.Dispatch','','','',2,'2010-08-18'),
(151,'edu.ualberta.med.biobank.model.DispatchSpecimen','edu.ualberta.med.biobank.model.DispatchSpecimen','edu.ualberta.med.biobank.model.DispatchSpecimen','','','',2,'2011-02-28'),
(170,'edu.ualberta.med.biobank.model.ResearchGroup','','edu.ualberta.med.biobank.model.ResearchGroup','','','',2,'2010-12-07'),
(171,'edu.ualberta.med.biobank.model.Request','','edu.ualberta.med.biobank.model.Request','','','',2,'2010-12-08'),
(175,'edu.ualberta.med.biobank.model.Report','','edu.ualberta.med.biobank.model.Report','','','',2,'2011-01-13'),
(176,'edu.ualberta.med.biobank.model.ReportFilter','','edu.ualberta.med.biobank.model.ReportFilter','','','',2,'2011-01-13'),
(177,'edu.ualberta.med.biobank.model.ReportFilterValue','','edu.ualberta.med.biobank.model.ReportFilterValue','','','',2,'2011-01-13'),
(178,'edu.ualberta.med.biobank.model.ReportColumn','','edu.ualberta.med.biobank.model.ReportColumn','','','',2,'2011-01-13'),
(179,'edu.ualberta.med.biobank.model.Entity','','edu.ualberta.med.biobank.model.Entity','','','',2,'2011-01-13'),
(180,'edu.ualberta.med.biobank.model.EntityFilter','','edu.ualberta.med.biobank.model.EntityFilter','','','',2,'2011-01-13'),
(181,'edu.ualberta.med.biobank.model.EntityColumn','','edu.ualberta.med.biobank.model.EntityColumn','','','',2,'2011-01-13'),
(182,'edu.ualberta.med.biobank.model.EntityProperty','','edu.ualberta.med.biobank.model.EntityProperty','','','',2,'2011-01-13'),
(183,'edu.ualberta.med.biobank.model.PropertyModifier','','edu.ualberta.med.biobank.model.PropertyModifier','','','',2,'2011-01-13'),
(184,'edu.ualberta.med.biobank.model.PropertyType','','edu.ualberta.med.biobank.model.PropertyType','','','',2,'2011-01-13'),
(185,'edu.ualberta.med.biobank.model.CollectionEvent','','edu.ualberta.med.biobank.model.CollectionEvent','','','',2,'2011-02-15'),
(186,'edu.ualberta.med.biobank.model.ProcessingEvent','','edu.ualberta.med.biobank.model.ProcessingEvent','','','',2,'2011-02-15'),
(187,'edu.ualberta.med.biobank.model.SpecimenType','edu.ualberta.med.biobank.model.SpecimenType','edu.ualberta.med.biobank.model.SpecimenType','','','',2,'2011-02-28'),
(188,'edu.ualberta.med.biobank.model.Center','','edu.ualberta.med.biobank.model.Center','','','',2,'2011-02-15'),
(192,'edu.ualberta.med.biobank.model.RequestSpecimen','','edu.ualberta.med.biobank.model.RequestSpecimen','','','',2,'2011-02-28'),
(193,'edu.ualberta.med.biobank.model.ShipmentInfo','edu.ualberta.med.biobank.model.ShipmentInfo','edu.ualberta.med.biobank.model.ShipmentInfo','','','',2,'2011-02-28'),
(195,'edu.ualberta.med.biobank.model.PrintedSsInvItem','','edu.ualberta.med.biobank.model.PrintedSsInvItem','','','',2,'2011-06-06'),
(196,'edu.ualberta.med.biobank.model.PrinterLabelTemplate','','edu.ualberta.med.biobank.model.PrinterLabelTemplate','','','',2,'2011-06-06'),
(197,'edu.ualberta.med.biobank.model.JasperTemplate','','edu.ualberta.med.biobank.model.JasperTemplate','','','',2,'2011-06-07'),
(296,'edu.ualberta.med.biobank.model.User',NULL,'edu.ualberta.med.biobank.model.User',NULL,NULL,NULL,2,'2012-03-27'),
(297,'edu.ualberta.med.biobank.model.Group',NULL,'edu.ualberta.med.biobank.model.Group',NULL,NULL,NULL,2,'2012-03-27'),
(298,'edu.ualberta.med.biobank.model.Principal',NULL,'edu.ualberta.med.biobank.model.Principal',NULL,NULL,NULL,2,'2012-03-27'),
(299,'edu.ualberta.med.biobank.model.Membership',NULL,'edu.ualberta.med.biobank.model.Membership',NULL,NULL,NULL,2,'2012-03-27'),
(300,'edu.ualberta.med.biobank.model.Permission',NULL,'edu.ualberta.med.biobank.model.Permission',NULL,NULL,NULL,2,'2012-03-27'),
(301,'edu.ualberta.med.biobank.model.Role',NULL,'edu.ualberta.med.biobank.model.Role',NULL,NULL,NULL,2,'2012-03-27'),
(302,'edu.ualberta.med.biobank.model.Comment',NULL,'edu.ualberta.med.biobank.model.Comment',NULL,NULL,NULL,2,'2012-03-27');

INSERT INTO `csm_user_pe` VALUES
(1,1,1);

-- add tester role
INSERT INTO `csm_role` VALUES
(8,'Object Full Access','has create/read/update/delete privileges on objects',2,1,'2010-10-20');

-- protection group for all classes
INSERT INTO `csm_protection_group` VALUES
(1,'Internal: All Objects','Contains Protection Element of each model object',2,0,'2011-03-11',NULL);

-- assign role + protection to user testuser
INSERT INTO csm_user_group_role_pg VALUES
(222,1,NULL,8,1,'2012-11-25');

-- association protection group / protection element
INSERT INTO `csm_pg_pe` VALUES
(1289,1,186,'0000-00-00'),
(1290,1,18,'0000-00-00'),
(1291,1,25,'0000-00-00'),
(1292,1,7,'0000-00-00'),
(1293,1,180,'0000-00-00'),
(1294,1,10,'0000-00-00'),
(1295,1,21,'0000-00-00'),
(1296,1,178,'0000-00-00'),
(1297,1,36,'0000-00-00'),
(1298,1,6,'0000-00-00'),
(1299,1,188,'0000-00-00'),
(1300,1,170,'0000-00-00'),
(1301,1,51,'0000-00-00'),
(1302,1,8,'0000-00-00'),
(1303,1,24,'0000-00-00'),
(1304,1,151,'0000-00-00'),
(1305,1,32,'0000-00-00'),
(1306,1,65,'0000-00-00'),
(1307,1,27,'0000-00-00'),
(1308,1,183,'0000-00-00'),
(1309,1,13,'0000-00-00'),
(1310,1,20,'0000-00-00'),
(1311,1,19,'0000-00-00'),
(1312,1,179,'0000-00-00'),
(1313,1,16,'0000-00-00'),
(1314,1,187,'0000-00-00'),
(1315,1,5,'0000-00-00'),
(1316,1,192,'0000-00-00'),
(1318,1,30,'0000-00-00'),
(1320,1,11,'0000-00-00'),
(1321,1,193,'0000-00-00'),
(1322,1,177,'0000-00-00'),
(1323,1,15,'0000-00-00'),
(1324,1,181,'0000-00-00'),
(1325,1,175,'0000-00-00'),
(1326,1,184,'0000-00-00'),
(1327,1,171,'0000-00-00'),
(1328,1,35,'0000-00-00'),
(1329,1,4,'0000-00-00'),
(1330,1,176,'0000-00-00'),
(1331,1,3,'0000-00-00'),
(1332,1,185,'0000-00-00'),
(1333,1,12,'0000-00-00'),
(1334,1,182,'0000-00-00'),
(1418,1,195,'0000-00-00'),
(1420,1,196,'0000-00-00'),
(1422,1,197,'0000-00-00'),
(1523,1,296,'2012-03-27'),
(1524,1,297,'2012-03-27'),
(1525,1,298,'2012-03-27'),
(1526,1,299,'2012-03-27'),
(1527,1,300,'2012-03-27'),
(1528,1,301,'2012-03-27'),
(1529,1,302,'2012-03-27');
--
-- The following entries are Common Set of Privileges
--
INSERT INTO `csm_privilege` VALUES
(1,'CREATE','This privilege grants permission to a user to create an entity. This entity can be an object, a database entry, or a resource such as a network connection','2009-07-22'),
(2,'ACCESS','This privilege allows a user to access a particular resource.  Examples of resources include a network or database connection, socket, module of the application, or even the application itself','2009-07-22'),
(3,'READ','This privilege permits the user to read data from a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to read data about a particular entry','2009-07-22'),
(4,'WRITE','This privilege allows a user to write data to a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to write data about a particular entity','2009-07-22'),
(5,'UPDATE','This privilege grants permission at an entity level and signifies that the user is allowed to update data for a particular entity. Entities may include an object, object attribute, database row etc','2009-07-22'),
(6,'DELETE','This privilege permits a user to delete a logical entity. This entity can be an object, a database entry, a resource such as a network connection, etc','2009-07-22'),
(7,'EXECUTE','This privilege allows a user to execute a particular resource. The resource can be a method, function, behavior of the application, URL, button etc','2009-07-22');

-- associate privileges to role
INSERT INTO csm_role_privilege VALUES
(19,8,1),
(18,8,3),
(20,8,5),
(17,8,6);

-- thick client version of flyway history

INSERT INTO `schema_version` VALUES
(1,1,'1','Base version','INIT','Base version',NULL,'root','2015-11-13 00:20:36',0,1),
(2,2,'1.1','Biobank v350','SPRING_JDBC','edu.ualberta.med.biobank.migration.V1_1__Biobank_v350',NULL,'dummy','2015-11-13 00:21:47',11573,1),
(3,3,'1.2','Biobank v360','SPRING_JDBC','edu.ualberta.med.biobank.migration.V1_2__Biobank_v360',NULL,'dummy','2015-11-13 00:21:50',3551,1),
(4,4,'1.3','Biobank v370','SPRING_JDBC','edu.ualberta.med.biobank.migration.V1_3__Biobank_v370',NULL,'dummy','2015-11-13 00:21:51',292,1),
(5,5,'1.4','Biobank v380','SPRING_JDBC','edu.ualberta.med.biobank.migration.V1_4__Biobank_v380',NULL,'dummy','2015-11-13 00:21:53',1725,1),
(6,6,'1.5','Biobank v390','SPRING_JDBC','edu.ualberta.med.biobank.migration.V1_5__Biobank_v390',NULL,'dummy','2015-11-13 00:22:00',7579,1),
(7,7,'1.6','Biobank v3100','SPRING_JDBC','edu.ualberta.med.biobank.migration.V1_6__Biobank_v3100',NULL,'dummy','2015-11-13 00:22:00',65,1);

set foreign_key_checks=1;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-15 19:04:23
