-- Modulo Peaje
-- Inicializacion de Tags
INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')));
INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('cb523e8c-4008-4f8f-ab0f-824c7e76865b', '-', '')));
# INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('3e5e26ce-eb0e-4f2c-9486-06ee54d1ebe2', '-', '')));
# INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('fd42dbdd-abfe-4aa8-8d0c-b31c656c6854', '-', '')));
# INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('f5b43adb-4c01-4d8e-8fec-c3fd6d523804', '-', '')));
# INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('23eccccf-3d3e-47eb-afb2-41adf784764e', '-', '')));


-- Inicializacion de Matriculas
INSERT INTO `toll_db`.`Toll_License_Plate` (`license_plate_number`) VALUES ('ABC-123');
# INSERT INTO `toll_db`.`Toll_License_Plate` (`license_plate_number`) VALUES ('DEF-456');
# INSERT INTO `toll_db`.`Toll_License_Plate` (`license_plate_number`) VALUES ('GHI-789');

-- Inicializacion de Tarifas
INSERT INTO `toll_db`.`Toll_Tariff` (`amount`, `tariff_type`) VALUES ('100', 'common');
INSERT INTO `toll_db`.`Toll_Tariff` (`amount`, `tariff_type`) VALUES ('80', 'preferential');


-- Modulo Cliente
-- Inicializacion de Credit Card
INSERT INTO `toll_db`.`ClientModule_CreditCard` (`expireDate`, `cardNumber`) VALUES ('2026-05-13', '5252-5435-1235-6576');

-- Inicializacion de Cuentas
INSERT INTO `toll_db`.`ClientModule_Account` (`accountNumber`, `creationDate`, `creditCard_id`, `DTYPE`) VALUES ('1234567890', '2024-05-29', '1', 'POSTPay');
INSERT INTO `toll_db`.`ClientModule_Account` (`accountNumber`, `balance`, `creationDate`, `DTYPE`) VALUES ('876543210', '200', '2024-05-27', 'PREPay');

-- Inicializacion de Toll Customer
INSERT INTO `toll_db`.`ClientModule_TollCustomer` (`POSTPay_id`) VALUES ('1');
INSERT INTO `toll_db`.`ClientModule_TollCustomer` (`PREPay_id`) VALUES ('2');

-- Inicializacion de Usuarios
INSERT INTO toll_db.ClientModule_User (DTYPE, ci, email, name, password, tollCustomer_id) VALUES('national', '6.632.876-8', 'pepe@mail.com', 'Pepe','1234', 2);
INSERT INTO toll_db.ClientModule_User (DTYPE, ci, email, name, password, tollCustomer_id) VALUES('foreign', '5.231.312-3', 'mario@mail.com', 'Mario','1234', 1);

-- Inicializacion de Tag
INSERT INTO `toll_db`.`ClientModule_Tag` (`uniqueId`) VALUES (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')));
INSERT INTO `toll_db`.`ClientModule_Tag` (`uniqueId`) VALUES (UNHEX(REPLACE('cb523e8c-4008-4f8f-ab0f-824c7e76865b', '-', '')));

-- Inicializacion de Matricula
INSERT INTO `toll_db`.`ClientModule_LicensePlate` (`licensePlateNumber`) VALUES ('ABC-123');

-- Inicializacion de Vehiculos
INSERT INTO `toll_db`.`ClientModule_Vehicle` (`LicencePlate_id`, `Tag_id`, `DTYPE`) VALUES ('1', '1', 'national');
INSERT INTO `toll_db`.`ClientModule_Vehicle` (`Tag_id`, `DTYPE`) VALUES ('2', 'foreign');

-- Inicializacion de Link
INSERT INTO `toll_db`.`ClientModule_Link` (`active`, `initialDate`, `Vehicle_id`, `user_id`) VALUES (TRUE, '2024-02-12', '1', '1');
INSERT INTO `toll_db`.`ClientModule_Link` (`active`, `initialDate`, `Vehicle_id`, `user_id`) VALUES (TRUE, '2024-03-12', '2', '2');


-- Modulo Metodo de Pago
-- Inicializacion de Tag
INSERT INTO `toll_db`.`Payment_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('cb523e8c-4008-4f8f-ab0f-824c7e76865b', '-', '')));

-- Inicializacion de Vehiculos
INSERT INTO `toll_db`.`Payment_Vehicle` (`Tag_id`, `DTYPE`) VALUES ('1', 'foreign');

-- Modulo Sucive
-- Inicializacion de Tag
INSERT INTO `toll_db`.`Sucive_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')));

-- Inicializacion de Matriculas
INSERT INTO `toll_db`.`Sucive_License_Plate` (`license_plate_number`) VALUES ('ABC-123');

-- Inicializacion de Vehiculos
INSERT INTO `toll_db`.`Sucive_Vehicle` (`Tag_id`, `LicencePlate_id`, `DTYPE`) VALUES ('1', '1', 'national');
