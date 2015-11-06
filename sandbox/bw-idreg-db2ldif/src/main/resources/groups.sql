
select
  c.companyId,
  c.companyName,
  u.userId
from
  companies c
  LEFT JOIN users u on c.companyId = u.companyId
WHERE
  u.userIsActive = 1
  and c.companyStatus = 'active'

