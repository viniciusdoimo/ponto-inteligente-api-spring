INSERT INTO `company` (`id`, `cnpj`, `update_date`, `creation_date`, `Company_name`) 
VALUES (NULL, '82198127000121', CURRENT_DATE(), CURRENT_DATE(), 'Vinicius Doimo Desenvolvedor');

INSERT INTO `employee` (`id`, `cpf`, `update_date`, `creation_date`, `email`, `name`, 
`profile`, `qtd_hours_lunch`, `qtd_hours_worked_day`, `password`, `hour_value`, `company_id`) 
VALUES (NULL, '16248890935', CURRENT_DATE(), CURRENT_DATE(), 'vinicius.doimo@admin.com', 'ADMIN', 'ROLE_ADMIN', NULL, NULL, 
'$2a$06$xIvBeNRfS65L1N17I7JzgefzxEuLAL0Xk0wFAgIkoNqu9WD6rmp4m', NULL, 
(SELECT `id` FROM `company` WHERE `cnpj` = '82198127000121'));