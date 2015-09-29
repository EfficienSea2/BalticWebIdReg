
SELECT
  substring_index(lower(userEmail), '@', 1) as userName,
  u.userId,
  u.userEmail,
  u.userFirstName,
  u.userLastName,
  u.userLogin,
  u.userTitle,
  u.userOfficePhone,
  u.userMobilePhone,
  u.userAddressLine1,
  u.userAddressLine2,
  u.userAddressZip,
  u.userAddressCity,
  u.userAddressState,
  u.countrycode,
  u.userWebsite,
  c.companyName
from
  users u
  LEFT JOIN companies c ON c.companyId = u.companyId
where
  u.userIsActive = 1
  and c.companyStatus = 'active'
