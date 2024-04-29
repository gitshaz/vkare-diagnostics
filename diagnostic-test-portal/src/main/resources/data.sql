INSERT INTO "STATUS" (ID, NAME) VALUES
(1, 'Active'),
(2, 'Inactive');

INSERT INTO "TEST_PACKAGE" (ID, NAME, DESCRIPTION) VALUES
(6, 'Basic Body Profile', 'Containing 40 Tests includes CBC, Creatinine, etc.'),
(7,  'Complete Body Profile', 'Containing 40 Tests of Basic Profile + 76 additional tests'),
(8, 'Full Vitamin Test', 'Includes All Body Vitamins'),
(9, 'Covid Test', 'Covid 19 Test'),
(10,  'SPC Test', 'Sodium, Potassion and Calcium Test');

INSERT INTO "DIAGNOSTIC_TEST" (ID, TEST_NAME, UPPER_VALUE_MALE, LOWER_VALUE_MALE, PREFERRED_VALUE_MALE, UPPER_VALUE_FEMALE, LOWER_VALUE_FEMALE, PREFERRED_VALUE_FEMALE, FOR_MALE, FOR_FEMALE, TEST_COST) VALUES
(85, 'Complete Blood Count (CBC)', 9, 15, 14, 10,15,14, true, false, 150),
(86, 'Vitamin A', 9, 15, 14, 10,15,14, true, false, 175),
(87, 'Covid 19 Test', 9, 15, 14, 10,15,14, true, false, 205),
(88, 'Sodium Test', 9, 15, 14, 10,15,14, true, false, 220),
(89, 'Potassium Test', 9, 15, 14, 10,15,14, true, false,350),
(90, 'Calcium Test', 9, 15, 14, 10,15,14, true, false, 320);

INSERT INTO "PACKAGE_TESTS_MAPPING" (ID, TEST_PACKAGE_ID, DIAGNOSTIC_TEST_ID) values
(91, 6, 85),
(92, 7, 85),
(93, 8, 86),
(94, 9, 87),
(95, 10, 88),
(96, 10, 89),
(97, 10, 90);

INSERT INTO "PATIENT" (ID, EMAIL, FIRST_NAME, LAST_NAME, STATUS_ID, GENDER, DOB) VALUES
(11, 'eula.lane@jigrormo.ye', 'Eula', 'Lane',  1, 'M', '2001-01-01'),
(12, 'barry.rodriquez@zun.mm', 'Barry', 'Rodriquez', 1, 'M', '2001-01-01'),
(13, 'eugenia.selvi@capfad.vn', 'Eugenia', 'Selvi', 1, 'M', '2001-01-01'),
(14, 'alejandro.miles@dec.bn', 'Alejandro', 'Miles',  1, 'M', '2001-01-01'),
(15, 'cora.tesi@bivo.yt', 'Cora', 'Tesi',  1, 'M', '2001-01-01'),
(16, 'marguerite.ishii@judbilo.gn', 'Marguerite', 'Ishii',  1, 'M', '2001-01-01'),
(17, 'mildred.jacobs@joraf.wf', 'Mildred', 'Jacobs', 1, 'M', '2001-01-01'),
(18, 'gene.goodman@kem.tl', 'Gene', 'Goodman',  1, 'M', '2001-01-01'),
(19, 'lettie.bennett@odeter.bb', 'Lettie', 'Bennett',  1, 'M', '2001-01-01'),
(20, 'mabel.leach@lisohuje.vi', 'Mabel', 'Leach',  1, 'M', '2001-01-01'),
(21, 'jordan.miccinesi@duod.gy', 'Jordan', 'Miccinesi',  1, 'M', '2001-01-01'),
(22, 'marie.parkes@nowufpus.ph', 'Marie', 'Parkes',  1, 'M', '2001-01-01'),
(23, 'rose.gray@kagu.hr', 'Rose', 'Gray',  1, 'M', '2001-01-01'),
(24, 'garrett.stokes@fef.bg', 'Garrett', 'Stokes',  1, 'M', '2001-01-01'),
(25, 'barbara.matthieu@derwogi.jm', 'Barbara', 'Matthieu',  1, 'M', '2001-01-01'),
(26, 'jean.rhodes@wehovuce.gu', 'Jean', 'Rhodes', 1, 'M', '2001-01-01'),
(27, 'jack.romoli@zamum.bw', 'Jack', 'Romoli',  1, 'M', '2001-01-01'),
(28, 'pearl.holden@dunebuh.cr', 'Pearl', 'Holden',  1, 'M', '2001-01-01'),
(29, 'belle.montero@repiwid.si', 'Belle', 'Montero',  1, 'M', '2001-01-01'),
(30, 'olive.molina@razuppa.ga', 'Olive', 'Molina',  1, 'M', '2001-01-01'),
(31, 'minerva.todd@kulmenim.ad', 'Minerva', 'Todd',  1, 'M', '2001-01-01'),
(32, 'bobby.pearson@ib.kg', 'Bobby', 'Pearson', 1, 'M', '2001-01-01'),
(33, 'larry.ciappi@ba.lk', 'Larry', 'Ciappi',  1, 'M', '2001-01-01'),
(34, 'ronnie.salucci@tohhij.lv', 'Ronnie', 'Salucci',  1, 'M', '2001-01-01'),
(35, 'walter.grossi@tuvo.sa', 'Walter', 'Grossi',  1, 'M', '2001-01-01'),
(60, 'reachsastrys@gmail.com', 'Gary', 'Osborne',  1, 'M', '2001-01-01');

INSERT INTO "USER_ROLE" (ID, ROLE_NAME) values
(61, 'PATIENT'),
(62, 'DOCTOR'),
(63, 'LAB_TESTER'),
(64, 'ADMIN'),
(65, 'DESK_STAFF');
INSERT INTO "USER_LOGIN" (ID, USER_ROLE_ID, USER_NAME, PASSWORD, STATUS_ID) values
(65, 61,'reachsastrys@gmail.com', 'gary@12345', 1),
(66, 62, 'doctor', 'doctor', 1),
(67, 63, 'labuser1', 'labuser1', 1),
(68, 64, 'sastry', 'sripada', 1),
(69, 65, 'staff1', 'staff1', 1);
INSERT INTO "PERMISSION" (ID, PERMISSION) values
(70, 'ViewReport'),
(71, 'ApproveRejectTestResults'),
(72, 'AddUpdTestResults'),
(73, 'FullAccess'),
(74, 'RegisterPatient'),
(75, 'UpdatePatient'),
(76, 'UpdateDoctor');
INSERT INTO "ROLE_PERMISSION" (ID, USER_ROLE_ID, PERMISSION_ID) values
(78, 61, 70),
(79, 62, 71),
(80, 63, 72),
(81, 64, 73),
(82, 65, 74),
(83, 65, 75),
(84, 65, 76);

INSERT INTO "DOCTOR" (ID, NAME, SPECIALITY, EMAIL, STATUS_ID) values
(98, 'Dr. Victor Kwalis', 'MBBS, MD, CARDIAC', 'doc@tor.com', 1),
(99, 'Dr. Nirmala Reddy', 'MBBS, MD, NEUROSURGEON', 'doc@tor2.com', 1),
(100, 'Dr. Srinivas Ranga', 'MBBS, MD', 'doc@tor3.com', 1),
(101, 'Dr. Rajakrishna Beriya', 'MBBS, MD', 'doc@tor4.com', 1),
(102, 'Dr. Kent Thomas', 'MBBS, MD', 'doc@tor5.com', 1);

INSERT INTO "ORDER_STATUS"(ID, NAME) values
(103, 'Accepted'),
(104, 'Cancelled'),
(105, 'In-Progress'),
(106, 'Doctor-Review'),
(107, 'Completed');

INSERT INTO "TEST_ORDER_SUMMARY" (ID, PATIENT_ID, ORDER_DATE, ORDER_STATUS_ID, LAST_UPDATED_DATE, PRIMARY_DOCTOR_ID, PRIMARY_DOCTOR_COMMENTS, SEC_DOCTOR_ID, SEC_DOCTOR_COMMENTS) values
(107, 60, '2024-03-09 03:14:07', 103, '2024-03-10 03:14:07', 100, null, 101, null);

INSERT INTO "TEST_ORDER_DETAIL" (ID, TEST_ORDER_SUMMARY_ID, DIAGNOSTIC_TEST_ID, RESULT_VALUE, IS_COMPLETED) values
(108, 107, 85, 98.5, false);