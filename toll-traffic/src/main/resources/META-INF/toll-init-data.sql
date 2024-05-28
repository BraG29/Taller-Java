-- Inicializacion de Tags
INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')));
INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('cb523e8c-4008-4f8f-ab0f-824c7e76865b', '-', '')));
INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('3e5e26ce-eb0e-4f2c-9486-06ee54d1ebe2', '-', '')));
INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('fd42dbdd-abfe-4aa8-8d0c-b31c656c6854', '-', '')));
INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('f5b43adb-4c01-4d8e-8fec-c3fd6d523804', '-', '')));
INSERT INTO `toll_db`.`Toll_Tag` (`unique_id`) VALUES (UNHEX(REPLACE('23eccccf-3d3e-47eb-afb2-41adf784764e', '-', '')));


--Inicializacion de Matriculas
INSERT INTO `toll_db`.`Toll_License_Plate` (`license_plate_number`) VALUES ('ABC-123');
INSERT INTO `toll_db`.`Toll_License_Plate` (`license_plate_number`) VALUES ('DEF-456');
INSERT INTO `toll_db`.`Toll_License_Plate` (`license_plate_number`) VALUES ('GHI-789');

-- Inicializacion de Vehiculos
-- INSERT INTO `toll_db`.`Toll_National_Vehicle` (`license_plate`, `tag`) VALUES ('1', '1');
-- INSERT INTO `toll_db`.`Toll_National_Vehicle` (`license_plate`, `tag`) VALUES ('2', '3');
-- INSERT INTO `toll_db`.`Toll_National_Vehicle` (`license_plate`, `tag`) VALUES ('3', '5');
-- INSERT INTO `toll_db`.`Toll_Foreign_Vehicle` (`tag`) VALUES ('2');
-- INSERT INTO `toll_db`.`Toll_Foreign_Vehicle` (`tag`) VALUES ('4');
-- INSERT INTO `toll_db`.`Toll_Foreign_Vehicle` (`tag`) VALUES ('6');

-- Inicializacion de Tarifas
INSERT INTO `toll_db`.`Toll_Tariff` (`amount`, `tariff_type`) VALUES ('100', 'common');
INSERT INTO `toll_db`.`Toll_Tariff` (`amount`, `tariff_type`) VALUES ('80', 'preferential');