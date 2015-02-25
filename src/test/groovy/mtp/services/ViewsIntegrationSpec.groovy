package mtp.services

import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

import mtp.MtpConfiguration
import mtp.dataobjects.Entry

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader, classes=MtpConfiguration)
@IntegrationTest
class ViewsIntegrationSpec extends Specification {

	@Autowired
	private ApplicationContext appContext;
	
	DatastoreServiceImpl service
	
	def setup(){
		def builder = {String string -> new ConcurrentHashMap<String, Result>()}
		service = new DatastoreServiceImpl(new HashMap<String, Entry>(), builder );
		service.setAppContext(appContext)
	}
	
	def 'countries view should return originatingCountry of the entries'(){
		setup:
			service.put(new Entry(userId:10, timePlaced:LocalDateTime.now(), originatingCountry:"IT"))
			service.put(new Entry(userId:10, timePlaced:LocalDateTime.now(), originatingCountry:"EN"))
		when:
			def res = service.getView("countries",false,null)
		then:
			res.size()==2
			res*.key.flatten().sort() == ["EN","IT"]
	}
	
	def 'countries view should count entries originatingCountry when group is enabled'(){
		setup:
			service.put(new Entry(userId:10, timePlaced:LocalDateTime.now(), originatingCountry:"IT"))
			service.put(new Entry(userId:10, timePlaced:LocalDateTime.now(), originatingCountry:"IT"))
			service.put(new Entry(userId:10, timePlaced:LocalDateTime.now(), originatingCountry:"EN"))
		when:
			def res = service.getView("countries",true,1)
		then:
			res.size()==2
			res == ["EN":1,"IT":2]
	}
	
	def 'countries view should work even if a view is triggered before a put'(){
		setup:
			service.put(new Entry(userId:10, timePlaced:LocalDateTime.now(), originatingCountry:"IT"))
			service.put(new Entry(userId:10, timePlaced:LocalDateTime.now(), originatingCountry:"IT"))
			service.getView("countries",false,null)
			service.put(new Entry(userId:10, timePlaced:LocalDateTime.now(), originatingCountry:"EN"))
		when:
			def res = service.getView("countries",true,1)
		then:
			res.size()==2
			res == ["EN":1,"IT":2]
	}
}
