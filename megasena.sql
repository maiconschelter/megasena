/*top six numbers sorted*/
SELECT * FROM (
  SELECT COUNT(sorted), sorted
    FROM (
      SELECT unnest(resnumber) AS sorted
        FROM public.results) AS parser
   GROUP BY sorted
   ORDER BY 1 DESC
   LIMIT 6
) AS origem
ORDER BY 2 ASC

/*sorts having top numbers*/
SELECT *
  FROM public.results
 WHERE (resnumber @> '{10}' AND resnumber @> '{5}')
   AND (resnumber @> '{53}' OR resnumber @> '{23}' OR resnumber @> '{54}' OR resnumber @> '{4}')

/*the magic numbers*/
SELECT *
  FROM public.results
 WHERE resnumber @> '{4,5,10,46,53,54}'