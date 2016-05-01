
CREATE TABLE [dbo].[Prescription_Transaction_New](
	[Pre_id] [decimal](18, 0) NOT NULL,
	[P_id] [decimal](18, 0) NOT NULL,
	[sym_nm] [varchar](6000) NULL,
	[Med_nm] [varchar](6000) NULL,
	[Ex_date] [smalldatetime] NULL,
	[Fee_Code] [char](5) NULL,
	[PaidAmt] [decimal](18, 0) NULL,
	[OS_Bal] [decimal](18, 0) NULL,
	[Net_Payable] [decimal](18, 0) NULL,
	[Recevied_Amt] [decimal](18, 0) NULL,
	primary key (Pre_id, P_id)
) 
GO

INSERT INTO Prescription_Transaction_New select * FROM Prescription_Transaction
GO

DROP TABLE Prescription_Transaction
GO

CREATE TABLE [dbo].[Prescription_Transaction](
	[Pre_id] [decimal](18, 0) NOT NULL,
	[P_id] [decimal](18, 0) NOT NULL,
	[sym_nm] [varchar](6000) NULL,
	[Med_nm] [varchar](6000) NULL,
	[Ex_date] [smalldatetime] NULL,
	[Fee_Code] [char](5) NULL,
	[PaidAmt] [decimal](18, 0) NULL,
	[OS_Bal] [decimal](18, 0) NULL,
	[Net_Payable] [decimal](18, 0) NULL,
	[Recevied_Amt] [decimal](18, 0) NULL,
	primary key (Pre_id, P_id)
) 
GO

INSERT INTO Prescription_Transaction select * FROM Prescription_Transaction_New
GO

DROP TABLE Prescription_Transaction_New
GO

ALTER TABLE Prescription_Transaction ADD Adv_nm [varchar](6000) NULL
GO

CREATE TABLE [dbo].[Patient_Master_New](
	[Pid] [decimal](18, 0) NOT NULL,
	[PFName] [varchar](50) NULL,
	[PMName] [varchar](50) NULL,
	[PLName] [varchar](100) NULL,
	[Address] [varchar](2000) NULL,
	[City] [varchar](20) NULL,
	[Pin] [decimal](18, 0) NULL,
	[TPhone] [varchar](50) NULL,
	[MPhone] [varchar](50) NULL,
	[DrReference] [varchar](200) NULL,
	[email_id] [varchar](100) NULL,
	[BirthDate] [nvarchar](50) NULL,
	[Age] [decimal](18, 0) NULL,
	[active] [char](1) NULL,
	[Pre_Bal] [decimal](18, 0) NULL,
	primary key (Pid)
)
GO

INSERT INTO Patient_Master_New select * FROM Patient_Master
GO

DROP TABLE Patient_Master
GO

CREATE TABLE [dbo].[Patient_Master](
	[Pid] [decimal](18, 0) NOT NULL,
	[PFName] [varchar](50) NULL,
	[PMName] [varchar](50) NULL,
	[PLName] [varchar](100) NULL,
	[Address] [varchar](2000) NULL,
	[City] [varchar](20) NULL,
	[Pin] [decimal](18, 0) NULL,
	[TPhone] [varchar](50) NULL,
	[MPhone] [varchar](50) NULL,
	[DrReference] [varchar](200) NULL,
	[email_id] [varchar](100) NULL,
	[BirthDate] [nvarchar](50) NULL,
	[Age] [decimal](18, 0) NULL,
	[active] [char](1) NULL,
	[Pre_Bal] [decimal](18, 0) NULL,
	primary key (Pid))
GO

INSERT INTO Patient_Master select * FROM Patient_Master_New
GO

DROP TABLE Patient_Master_New
GO

delete from Prescription_Transaction where P_id in (select Pid from patient_master where PFName is null and PMName is null and PLName is null)
GO

delete from Patient_Master where PFName is null and PMName is null and PLName is null
GO

ALTER TABLE Patient_Master ADD [birth_date] [smalldatetime] NULL
GO

update Patient_Master set BirthDate='9/23/2006 12:00:00 AM' where Pid=43232
GO

update Patient_Master set BirthDate='10/24/1965 00:00:00' where Pid=41405
GO

update Patient_Master set BirthDate='08/24/1976 00:00:00' where Pid=15051
GO

update Patient_Master set BirthDate='12/06/1964 00:00:00' where Pid=17452
go
update Patient_Master set BirthDate='10/05/1990 00:00:00' where Pid=17486
go
update Patient_Master set BirthDate='06/01/1998 00:00:00' where Pid=18410
go
update Patient_Master set BirthDate='03/04/1945 00:00:00' where Pid=18445
go
update Patient_Master set BirthDate='06/10/1971 00:00:00' where Pid=18656
go
update Patient_Master set BirthDate='02/09/1954 00:00:00' where Pid=18728
go
update Patient_Master set BirthDate='07/24/1978 00:00:00' where Pid=19392
go
update Patient_Master set BirthDate='07/12/1979 00:00:00' where Pid=19427
go
update Patient_Master set BirthDate='01/01/1923 00:00:00' where Pid=21324
go
update Patient_Master set BirthDate='09/23/1954 00:00:00' where Pid=24035
go
update Patient_Master set BirthDate='06/26/1944 00:00:00' where Pid=24464
go
update Patient_Master set BirthDate='07/19/2002 00:00:00' where Pid=24771
go
update Patient_Master set BirthDate='11/18/1968 00:00:00' where Pid=26067
go
update Patient_Master set BirthDate='01/01/1965 00:00:00' where Pid=26264
go

ALTER TABLE Prescription_Transaction add DEFAULT -1 for Pre_id
GO

