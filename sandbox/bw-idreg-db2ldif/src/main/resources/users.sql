
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
  c.companyName,
  CASE WHEN u.userImage != ''
    THEN concat('https://tw-',u.userImageUploadedToServer,'.teamworkpm.net/sites/EfficienSea2/images/users/',u.userImage)
    ELSE u.userS3ImagePath
  END as userImage
from
  users u
  LEFT JOIN companies c ON c.companyId = u.companyId
where
  u.userIsActive = 1
  and c.companyStatus = 'active'
